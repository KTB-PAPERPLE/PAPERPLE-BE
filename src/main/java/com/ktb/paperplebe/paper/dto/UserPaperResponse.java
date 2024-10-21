package com.ktb.paperplebe.paper.dto;

import com.ktb.paperplebe.paper.entity.Paper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record UserPaperResponse(
        Long paperId,
        String content,
        String newspaperLink,
        List<String> tags,
        String newspaperSummary,
        String image,
        LocalDateTime createdAt,
        String nickname,
        String profileImage,
        boolean isLikedByCurrentUser
) {
    public static UserPaperResponse of(final Paper paper, final String nickname, final String profileImage, final Optional<Boolean> isLikedByCurrentUser) {
        return new UserPaperResponse(
                paper.getId(),
                paper.getContent(),
                paper.getNewspaperLink(),
                paper.getTags(),
                paper.getNewspaperSummary(),
                paper.getImage(),
                paper.getCreatedAt(),
                nickname,
                profileImage,
                isLikedByCurrentUser.orElse(false)
        );
    }
}
