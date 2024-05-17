package kr.co.lotte.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.ReviewDTO;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.dto.UserUpdateDTO;
import kr.co.lotte.entity.Review;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AdminService adminService;


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


        //배너 출력
        List<BannerDTO> banner5 = adminService.validateBanner("MY1");
        log.info("banner5: {}", banner5);
        model.addAttribute("banner5", banner5);

        return "/my/info";
    }

    @PostMapping("/my/passwordChange")
    public String passwordChange(@RequestParam("pass1") String pass1, @RequestParam("uid") String uid) {
        log.info("pass1={}",pass1);
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        userDTO.setPass(pass1);
        memberService.updateUserPassword(userDTO);
        return "redirect:/my/info?code=" + 100 + "&uid=" + uid;
    }


    // 회원 휴대폰, 이메일 변경   API
    @ResponseBody
    @GetMapping("/my/{type}/{value}/{uid}")
    public ResponseEntity<?> changeUser(@PathVariable("type") String type,
                                        @PathVariable("value") String value,
                                        @PathVariable("uid") String uid) {
        log.info("uid={} type={} value={}", uid, type, value);
        return memberService.myInfoUpdate(type, value, uid);
    }

    //회원 주소 변경 요청 API
    @ResponseBody
    @PutMapping("/my/modifyAddr")
    public ResponseEntity<?> modifyAddr(@RequestBody UserDTO userDTO) {
        return memberService.updateUserAddr(userDTO);
    }


    //회원 탈퇴
    @GetMapping("/my/delUser/{uid}")
    public ResponseEntity<?> delUser(@PathVariable("uid")String uid){
        //status 0으로 변환 및 권한 None으로 변경

        log.info("uid={}", uid);

        int result = memberService.findUserForDel(uid);//회원아이디로 회원데이터 불러오기

        Map<String, String> map1 = new HashMap<>();

        if(result==1){
            map1.put("result", "success");
        }else{
            map1.put("result", "fail");
        }

        return ResponseEntity.ok().body(map1);
    }

}
