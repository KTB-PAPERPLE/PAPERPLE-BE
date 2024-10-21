package com.ktb.paperplebe.newsPaper.dto;

import com.ktb.paperplebe.newsPaper.entity.NewsPaper;

import java.time.LocalDateTime;

public record NewsPaperResponse(
        Long newsPaperId,
        String title,
        String link,
        String image,
        String summary,
        LocalDateTime createdAt,
        LocalDateTime publishedAt
) {
    public static NewsPaperResponse of(final NewsPaper newsPaper) {
        return new NewsPaperResponse(
                newsPaper.getId(),
                newsPaper.getTitle(),
                newsPaper.getLink(),
                newsPaper.getImage(),
                newsPaper.getSummary(),
                newsPaper.getCreatedAt(),
                newsPaper.getPublishedAt()
        );
    }
}
