package kr.co.lotte.controller;

import kr.co.lotte.entity.Policy;
import kr.co.lotte.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/policy/buyer")
    public String buyerPolicy(Model model) {
        Policy policy = policyService.buyerPolicy(1L);
        model.addAttribute("policy", policy);
        return "/policy/buyer";
    }
    @GetMapping("/policy/seller")
    public String sellerPolicy(Model model) {
        Policy policy = policyService.buyerPolicy(2L);
        model.addAttribute("policy", policy);
        return "/policy/seller";
    }
    @GetMapping("/policy/finance")
    public String financePolicy(Model model) {
        Policy policy = policyService.buyerPolicy(3L);
        model.addAttribute("policy", policy);
        return "/policy/finance";
    }
    @GetMapping("/policy/location")
    public String locationPolicy(Model model) {
        Policy policy = policyService.buyerPolicy(4L);
        model.addAttribute("policy", policy);
        return "/policy/location";
    }
    @GetMapping("/policy/privacy")
    public String privacyPolicy(Model model) {
        Policy policy = policyService.buyerPolicy(5L);
        model.addAttribute("policy", policy);
        return "/policy/privacy";
    }


}
