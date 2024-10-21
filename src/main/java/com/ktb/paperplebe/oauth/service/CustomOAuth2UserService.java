package com.ktb.paperplebe.oauth.service;


import com.ktb.paperplebe.oauth.OAuth2CustomUser;
import com.ktb.paperplebe.oauth.OAuthAttributes;
import com.ktb.paperplebe.oauth.constant.SocialType;
import com.ktb.paperplebe.user.entity.User;
import com.ktb.paperplebe.user.repository.UserRepository;
import com.ktb.paperplebe.user.service.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 인증 처리 시작 - 사용자 정보 로드 중 (CustomOAuth2UserService.loadUser())");

        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = SocialType.getSocialType(registrationId);
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, oAuth2User.getAttributes());
        User user = userRepository.findBySocialTypeAndSocialId(socialType, extractAttributes.getOauth2UserInfo().getId())
                .orElseGet(() -> saveUser(extractAttributes, socialType));

        return new OAuth2CustomUser(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                user
        );
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        createdUser.updateNickname(nicknameGenerator.generateRandomNickname());
        return userRepository.save(createdUser);
    }
}
