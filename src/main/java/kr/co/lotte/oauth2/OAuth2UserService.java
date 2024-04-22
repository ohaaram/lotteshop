package kr.co.lotte.oauth2;

import kr.co.lotte.dto.oauth2.GoogleResponse;
import kr.co.lotte.dto.oauth2.KakaoResponse;
import kr.co.lotte.dto.oauth2.NaverResponse;
import kr.co.lotte.dto.oauth2.OAuth2Response;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        //네이버, 구글 어떤 provider 인지 출력
        String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("provider={}", provider);

        log.info("userRequest={}", userRequest);

        // 사용자 확인 및 회원가입 처리
        OAuth2Response oAuth2Response = null;

        if (provider.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (provider.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else if (provider.equals("kakao")){

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        } else {

            return null;
        }

        String createOAuth2Uid = (oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId()).substring(0,16);

        String role = "USER";
        User user = userRepository.findById(createOAuth2Uid)
                .orElse(User.builder()
                        .uid(createOAuth2Uid)
                        .email(oAuth2Response.getEmail())
                        .name(oAuth2Response.getName())
                        .nick(oAuth2Response.getName())
                        .provider(oAuth2Response.getProvider())
                        .role("USER")
                        .build());

        log.info("user={}", user);
        userRepository.save(user);


        return new CustomOAuth2User(oAuth2Response, role);
    }
}