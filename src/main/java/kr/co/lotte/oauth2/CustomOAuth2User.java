package kr.co.lotte.oauth2;

import kr.co.lotte.dto.oauth2.OAuth2Response;
import kr.co.lotte.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;
    //entity  선언
    private User user;

    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return role;
            }

        });
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    public String createOauth2Username() {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }
}

