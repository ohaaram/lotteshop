package kr.co.lotte.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PolicyController {

    @GetMapping("/policy/buyer")
    public String buyerPolicy() {
        return "/policy/buyer";
    }
    @GetMapping("/policy/seller")
    public String sellerPolicy() {
        return "/policy/seller";
    }
    @GetMapping("/policy/finance")
    public String financePolicy() {
        return "/policy/finance";
    }
    @GetMapping("/policy/location")
    public String locationPolicy() {
        return "/policy/location";
    }
    @GetMapping("/policy/privacy")
    public String privacyPolicy() {
        return "/policy/privacy";
    }


}
