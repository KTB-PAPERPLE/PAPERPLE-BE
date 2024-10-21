package com.ktb.paperplebe.paper.service;

import com.ktb.paperplebe.paper.entity.Paper;
import com.ktb.paperplebe.paper.entity.PaperLike;
import com.ktb.paperplebe.paper.exception.PaperLikeException;
import com.ktb.paperplebe.paper.repository.PaperLikeRepository;
import com.ktb.paperplebe.paper.repository.PaperRepository;
import com.ktb.paperplebe.user.entity.User;
import com.ktb.paperplebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ktb.paperplebe.paper.exception.PaperLikeErrorCode.PAPER_ALREADY_LIKED;
import static com.ktb.paperplebe.paper.exception.PaperLikeErrorCode.PAPER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class PaperLikeService {

    private final PaperLikeRepository paperLikeRepository;
    private final PaperRepository paperRepository;
    private final UserService userService;

    public void increaseLikeCount(Long userId, Long paperId) {
        if (paperLikeRepository.existsByPaper_Id(paperId)) {
            throw new PaperLikeException(PAPER_ALREADY_LIKED);
        }


        Paper findPaper = findPaperOrThrow(paperId);
        User findUser = userService.findById(userId);

        PaperLike like = new PaperLike(findPaper, findUser);
        findPaper.addLike(like);

        paperLikeRepository.save(like);
    }

    public void decreaseLikeCount(Long userId, Long paperId) {
        PaperLike like = paperLikeRepository.findByPaper_IdAndUser_Id(paperId, userId)
                .orElseThrow(() -> new IllegalStateException("This paper was not previously liked by this user."));

        Paper findPaper = findPaperOrThrow(paperId);

        findPaper.removeLike(like);

        paperLikeRepository.delete(like);
    }

    public Map<Long, Boolean> getLikeStatus(Long userId, List<Long> paperIds) {
        List<PaperLike> paperLikes = paperLikeRepository.findByPaper_IdInAndUser_Id(paperIds, userId);
        Map<Long, Boolean> result = new HashMap<>();

        paperLikes.forEach(paperLike -> result.put(paperLike.getPaper().getId(), true));
        paperIds.forEach(paperId -> result.putIfAbsent(paperId, false));
        return result;
    }

    private Paper findPaperOrThrow(Long paperId) {
        return paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperLikeException(PAPER_NOT_FOUND));
    }
}