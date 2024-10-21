package com.ktb.paperplebe.paper.controller;

import com.ktb.paperplebe.common.dto.SuccessResponse;
import com.ktb.paperplebe.paper.service.PaperLikeFacade;
import com.ktb.paperplebe.paper.service.PaperLikeService;
import com.ktb.paperplebe.user.constant.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/paper")
@RestController
public class PaperLikeController {

    private final PaperLikeService paperLikeService;
    private final PaperLikeFacade paperLikeFacade;

    @PostMapping("/{paperId}/likes")
    @Secured(UserRole.ROLE_USER_VALUE)
    public SuccessResponse increaseLikeCount(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long paperId) throws InterruptedException {
        paperLikeFacade.increaseLikeCount(
                userId,
                paperId);
        return new SuccessResponse();
    }

    @DeleteMapping("/{paperId}/likes")
    @Secured(UserRole.ROLE_USER_VALUE)
    public SuccessResponse decreaseLikeCount(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long paperId) throws InterruptedException {
        paperLikeFacade.decreaseLikeCount(
                userId,
                paperId);
        return new SuccessResponse();
    }

    @GetMapping("/likes")
    @Secured(UserRole.ROLE_USER_VALUE)
    public Map<Long, Boolean> getLikeStatus(
            @AuthenticationPrincipal Long userId,
            @RequestParam("paperIds") List<Long> paperIds) {
        return paperLikeService.getLikeStatus(
                userId,
                paperIds);
    }
}
