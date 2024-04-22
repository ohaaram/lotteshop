package kr.co.lotte.dto.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> atttribute;

    public KakaoResponse(Map<String, Object> atttribute) {
        this.atttribute = atttribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return atttribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return atttribute.get("id").toString()+"@hanmail.net";
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) atttribute.get("properties");
        return properties.get("nickname").toString();

    }
}
