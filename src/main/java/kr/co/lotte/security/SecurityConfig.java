package kr.co.lotte.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.oauth2.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    private final SecurityUserService securityUserService;//이거 fileterChain 괄호안 httpSecutiry옆에 있던거 옮김

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 로그인 설정
        httpSecurity.formLogin(login -> login //Form 로그인 인증 기능이 작동함
                .loginPage("/member/login") // 로그인 페이지
                .failureUrl("/member/login?success=100") // 로그인 실패 후 이동 페이지
                .usernameParameter("uid") //아이디 파라미터명 설정
                .passwordParameter("pass") // 패스워드 파라미터 설정
                .successHandler(new CustomForRedirect())); // 로그인 성공 후 이동페이지


        // 로그아웃 설정
        httpSecurity.logout(logout -> logout
                .invalidateHttpSession(true) 
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/member/login?success=300"));

        /*
            인가 설정
             - Spring Security는 존재하지 않는 요청 주소에 대해 기본적으로 login 페이지로 redirect를 수행
             - 자원 요청의 추가 인가 처리 확장과 redirect 기본 해제를 위해 마지막에 .anyRequest().permitAll() 설정
         */
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/member/**").permitAll()
                .requestMatchers("/article/**").permitAll()
                .requestMatchers("/market/list").permitAll()
                .requestMatchers("/market/view").permitAll()
                .requestMatchers("/market/**").authenticated()
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "MANAGER")
                .requestMatchers("/manager/**").hasAnyAuthority("ADMIN")
                .anyRequest().permitAll());

        // 사이트 위변조 방지 설정
        httpSecurity.csrf(CsrfConfigurer::disable);

        //OAuth 설정
        //OAuth 설정
        httpSecurity.oauth2Login(config -> config
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .userInfoEndpoint((userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(oAuth2UserService))));

        //세션 고정 보호
        httpSecurity.sessionManagement((auth) -> auth
                .sessionFixation()
                //로그인 시 동일한 세션에 대한 id 변경
                .changeSessionId());


        // 자동로그인 설정
        // rememberMe 쿠키 확인
        httpSecurity.rememberMe(config -> config.userDetailsService(securityUserService)
                .rememberMeParameter("rememberMe")
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400));// 자동 로그인 유효 기간 (초)) 24시간

        return httpSecurity.build();
    }

    // Security 인증 암호화 인코더 설정

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //권한 계층 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ADMIN > MANAGER > USER");

        return hierarchy;
    }


}