package com.ktb.paperplebe.paper.entity;

import com.ktb.paperplebe.common.entity.BaseEntity;
import com.ktb.paperplebe.paper.util.ListToJsonConverter;
import com.ktb.paperplebe.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Paper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 255)
    private String newspaperLink;

    @Column(length = 255)
    private String newspaperSummary;

    @Column(name = "tags", columnDefinition = "json")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> tags;

    @Column(length = 255)
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaperLike> likes = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Paper(final String content, final String newspaperLink, final List<String> tags, final String newspaperSummary, final String image, final User user) {
        this.content = content;
        this.newspaperLink = newspaperLink;
        this.tags = tags;
        this.newspaperSummary = newspaperSummary;
        this.image = image;
        this.user = user;
    }

    public static Paper of(final String content, final String newspaperLink, final List<String> tags, final String newspaperSummary, final String image, final User user) {
        return new Paper(content, newspaperLink, tags, newspaperSummary, image, user);
    }

    // 좋아요 수 관련 메서드
    public void addLike(final PaperLike paperLike) {
        likes.add(paperLike);
        paperLike.assignPaper(this);
    }

    public void removeLike(final PaperLike paperLike) {
        likes.remove(paperLike);
        paperLike.removePaper();
    }


    public void updateContent(final String content) {
        if (content != null && !content.isEmpty()) {
            this.content = content;
        }
    }

    public void updateNewspaperLink(final String newspaperLink) {
        if (newspaperLink != null && !newspaperLink.isEmpty()) {
            this.newspaperLink = newspaperLink;
        }
    }

    public void updateNewspaperSummary(final String newspaperSummary) {
        this.newspaperSummary = newspaperSummary;
    }

    public void updateImage(final String image) {
        this.image = image;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
