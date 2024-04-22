package kr.co.lotte.dto.oauth2;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> atttribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.atttribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return atttribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return atttribute.get("email").toString();
    }

    @Override
    public String getName() {
        return atttribute.get("name").toString();
    }
}