package com.ktb.paperplebe.paper.service;

import com.ktb.paperplebe.paper.dto.PaperRequest;
import com.ktb.paperplebe.paper.dto.PaperResponse;
import com.ktb.paperplebe.paper.dto.UserPaperResponse;
import com.ktb.paperplebe.paper.entity.Paper;
import com.ktb.paperplebe.paper.exception.PaperException;
import com.ktb.paperplebe.paper.repository.PaperRepository;
import com.ktb.paperplebe.user.entity.User;
import com.ktb.paperplebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ktb.paperplebe.paper.exception.PaperErrorCode.PAPER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaperService {
    private final PaperRepository paperRepository;
    private final UserService userService;
    private final PaperLikeService paperLikeService;

    @Transactional
    public PaperResponse createPaper(PaperRequest paperRequest, Long userId) {
        User user = userService.findById(userId);

        Paper paper = Paper.of(
                paperRequest.content(),
                paperRequest.newspaperLink(),
                paperRequest.tags(),
                paperRequest.newspaperSummary(),
                paperRequest.image(),
                user
        );

        Paper savedPaper = paperRepository.save(paper);
        return PaperResponse.of(savedPaper, user);
    }

    @Transactional
    public PaperResponse updatePaper(Long paperId, PaperRequest paperRequest) {
        Paper paper = findPaperByIdOrThrow(paperId);

        paper.updateContent(paperRequest.content());
        paper.updateNewspaperLink(paperRequest.newspaperLink());
        //paper.updateView(paperRequest.view());
        paper.updateNewspaperSummary(paperRequest.newspaperSummary());
        paper.updateImage(paperRequest.image());

        paperRepository.save(paper);
        return PaperResponse.of(paper, paper.getUser());
    }

    public PaperResponse getPaper(Long paperId) {
        Paper paper = findPaperByIdOrThrow(paperId);
        return PaperResponse.of(paper, paper.getUser());
    }

    public List<PaperResponse> getPaperList(Pageable pageable, String orderBy) {
        List<Paper> papers = paperRepository.findAllOrderByLikesOrCreatedAt(pageable, orderBy);

        return papers.stream()
                .map(paper -> PaperResponse.of(paper, paper.getUser()))
                .collect(Collectors.toList());
    }
      
    public List<UserPaperResponse> getMyPapers(Long userId) {
        User user = userService.findById(userId);
        List<Paper> papers = paperRepository.findByUserId(userId);
        return papers.stream()
                .map(paper -> UserPaperResponse.of(paper, user.getNickname(), user.getProfileImage(), Optional.empty()))
                .collect(Collectors.toList());
    }

    public List<UserPaperResponse> getPapersByUser(Long userId, Long currentUserId) {
        List<Paper> papers = paperRepository.findByUserId(userId);

        List<Long> paperIds = papers.stream().map(Paper::getId).collect(Collectors.toList());
        Map<Long, Boolean> likeStatusMap = paperLikeService.getLikeStatus(currentUserId, paperIds);

        return papers.stream()
                .map(paper -> {
                    boolean isLikedByCurrentUser = likeStatusMap.getOrDefault(paper.getId(), false);
                    return UserPaperResponse.of(paper, paper.getUser().getNickname(), paper.getUser().getProfileImage(), Optional.of(isLikedByCurrentUser));
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePaper(Long paperId) {
        if (!paperRepository.existsById(paperId)) {
            throw new PaperException(PAPER_NOT_FOUND);
        }
        paperRepository.deleteById(paperId);
    }

    private Paper findPaperByIdOrThrow(Long paperId) {
        return paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperException(PAPER_NOT_FOUND));
    }

}
