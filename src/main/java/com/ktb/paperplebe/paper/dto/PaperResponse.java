package com.ktb.paperplebe.paper.dto;

import com.ktb.paperplebe.paper.entity.Paper;
import com.ktb.paperplebe.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public record PaperResponse(
        Long paperId,
        String content,
        String newspaperLink,
        List<String> tags,
        String newspaperSummary,
        String image,
        LocalDateTime createdAt,
        String nickname,
        String profileImage

) {
    public static PaperResponse of(final Paper paper, final User user) {
        return new PaperResponse(
                paper.getId(),
                paper.getContent(),
                paper.getNewspaperLink(),
                paper.getTags(),
                paper.getNewspaperSummary(),
                paper.getImage(),
                paper.getCreatedAt(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
