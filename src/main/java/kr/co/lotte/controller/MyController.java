package kr.co.lotte.controller;

import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.UserUpdateDTO;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AdminService adminService;

    @GetMapping("/my/coupon")
    public String myCoupon() {
        return "/my/coupon";
    }

    @GetMapping("/my/home")
    public String myHome(Model model) {

        List<BannerDTO> banner5 = adminService.findMY1("MY1");
        log.info("banner5: {}", banner5);
        model.addAttribute("banner5", banner5);

        return "/my/home";
    }


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

    @PostMapping("/my/info")
    public String myInfoUpdate(Principal principal, UserUpdateDTO userUpdateDTO, Model model) {
        model.addAttribute("userUpdateDTO", userUpdateDTO);
        memberService.myInfoUpdate(principal, userUpdateDTO);
        return "redirect:/my/info";

    }

    @GetMapping("/my/order")
    public String myOrder() {
        return "/my/order";
    }

    @GetMapping("/my/point")
    public String myPoint() {
        return "/my/point";
    }

    @GetMapping("/my/qna")
    public String myQna() {
        return "/my/qna";
    }

    @GetMapping("/my/review")
    public String myReview() {
        return "/my/review";
    }
}
