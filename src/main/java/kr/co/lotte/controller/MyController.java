package kr.co.lotte.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyController {

    @GetMapping("/my/coupon")
    public String myCoupon() {
        return "/my/coupon";
    }

    @GetMapping("/my/home")
    public String myHome() {
        return "/my/home";
    }

    @GetMapping("/my/info")
    public String myInfo() {
        return "/my/info";
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
