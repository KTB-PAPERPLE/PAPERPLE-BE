package com.ktb.paperplebe.oauth;

import com.ktb.paperplebe.user.constant.UserRole;
import com.ktb.paperplebe.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuth2CustomUser extends DefaultOAuth2User {

    private final User user;

    public OAuth2CustomUser(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey,
                            User user) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }
    public UserRole getMemberRole() {
        return user.getRole();
    }
}
