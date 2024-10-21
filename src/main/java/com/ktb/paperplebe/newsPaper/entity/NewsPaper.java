package com.ktb.paperplebe.newsPaper.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class NewsPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Column(nullable = false, length = 2048)
    private String link;

    @Column(unique = true, length = 255)
    private String linkHash;

    @Column(length = 2048)
    private String image;

    @Column(nullable = false, length = 255)
    private String source;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public NewsPaper(String title, String body, String summary, String link, String linkHash, String image, String source, LocalDateTime publishedAt, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.title = title;
        this.body = body;
        this.summary = summary;
        this.link = link;
        this.linkHash = linkHash;
        this.image = image;
        this.source = source;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
