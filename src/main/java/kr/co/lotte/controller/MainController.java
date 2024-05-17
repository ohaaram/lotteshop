package kr.co.lotte.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.Banner;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.User;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MainService mainService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model){

          mainService.upDateVisitor();
          //coupon 변경할것임
        mainService.CouponStateUpdate();
        //hit상품 변경
        mainService.updateHit();

        model.addAttribute("searches", mainService.findHotKeyword());

        List<BannerDTO> banner1 = adminService.validateBanner("MAIN1");
        List<BannerDTO> banner2 = adminService.validateBanner("MAIN2");
        model.addAttribute("prodSolds", mainService.selectHitProducts());

        model.addAttribute("prodRecommend", mainService.selectRecomendProducts());
        model.addAttribute("prodRecent", mainService.selectRecentProducts());
        model.addAttribute("prodDiscount", mainService.selectDiscountProducts());

       log.info("MainController - banner : "+banner1.toString());

        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);


        return "/index";
    }

    @PostMapping("/main/banner/index")
    public ResponseEntity<?> banner(@RequestBody Map<String, Object> map){
        
        log.info("banner가 컨트롤러에 안들어오네");

        String bannerNO = (String)map.get("bannerNo");

        log.info("bannerNO : "+bannerNO);

        Banner banner = adminService.findByIdBanner(bannerNO);

        log.info("조회수 올리고 난 다음 조회하기 : "+banner);

        Map<String, String> result = new HashMap<>();
        result.put("data","1");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/main/event")
    public String event(Model model){
        return "/product/event";
    }

    @ResponseBody
    @GetMapping("/main/reward")
    public ResponseEntity reward(Authentication authentication){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = myUserDetails.getUser();
        String uid= user.getUid();
        return mainService.getReward(uid);
    }
}
