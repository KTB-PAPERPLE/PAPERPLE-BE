package com.ktb.paperplebe.oauth;

import com.ktb.paperplebe.oauth.constant.SocialType;
import com.ktb.paperplebe.oauth.provider.KakaoUserInfo;
import com.ktb.paperplebe.oauth.provider.OAuth2UserInfo;
import com.ktb.paperplebe.user.constant.UserRole;
import com.ktb.paperplebe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oauth2UserInfo;

    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        // socialType은 항상 SocialType.KAKAO
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(userNameAttributeName, new KakaoUserInfo(attributes));
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .role(UserRole.ROLE_USER)
                .profileImage(oauth2UserInfo.getProfileImage())
                .build();
    }
}