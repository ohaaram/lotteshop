package kr.co.lotte.controller;

import kr.co.lotte.dto.UserUpdateDTO;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import kr.co.lotte.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;


    //회원정보 변경 폼 (GET)
    @GetMapping("/my/info")
    public String myInfo(Principal principal, Model model) {
        log.info("principal={}", principal);
        String loginId = principal.getName();
        log.info("loginId={}", loginId);
        Optional<User> findUser = memberRepository.findById(loginId);
        log.info("findUser={}", findUser);
        User user = findUser.get();
        log.info("user={}", user);
        model.addAttribute("user", user);

        return "/my/info";
    }


    @ResponseBody
    @GetMapping("/my/{type}/{value}/{uid}")
    public ResponseEntity<?> changeUser(@PathVariable("type") String type,
                                        @PathVariable("value") String value,
                                        @PathVariable("uid") String uid) {
        log.info("uid={} type={} value={}",uid, type, value );
        return memberService.myInfoUpdate(type, value, uid);
    }

}
