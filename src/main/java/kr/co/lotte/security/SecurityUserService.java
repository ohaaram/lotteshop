package kr.co.lotte.security;


import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private final MemberRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result= repository.findById(username);
        UserDetails userDetails =null;
        if(!result.isEmpty()){

            //해당하는 사용자가 존재하면 인증 객체 생성
            User user = result.get();
            userDetails = MyUserDetails.builder()
                    .user(user)
                    .build();
        }
        //Security ContextHolder 저장
        //사용자가 로그인을 진행한 뒤 사용자 정보는 SecurityContentHolder에 의해서 서버 세션에 관리된다.
        return userDetails;
    }

}
