package com.ktb.paperplebe.paper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaperLikeFacade {

    private final PaperLikeService paperLikeService;

    private void executeWithRetry(Runnable action) throws InterruptedException {
        while (true) {
            try {
                action.run();
                break;
            } catch (OptimisticLockingFailureException e) {
                Thread.sleep(100);
            }
        }
    }

    public void increaseLikeCount(Long userId, Long paperId) throws InterruptedException {
        executeWithRetry(() -> paperLikeService.increaseLikeCount(userId, paperId));
    }

    public void decreaseLikeCount(Long userId, Long paperId) throws InterruptedException {
        executeWithRetry(() -> paperLikeService.decreaseLikeCount(userId, paperId));
    }
}
