package com.ktb.paperplebe.user.entity;

import com.ktb.paperplebe.oauth.constant.SocialType;
import com.ktb.paperplebe.paper.entity.Paper;
import com.ktb.paperplebe.paper.entity.PaperLike;
import com.ktb.paperplebe.user.constant.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String profileImage;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paper> papers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaperLike> paperLikes;

    @Builder
    public User(String nickname, String socialId, UserRole role, SocialType socialType, String profileImage) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.role = role;
        this.socialType = socialType;
        this.profileImage = profileImage;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
