package kr.co.lotte.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.Banner;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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


        List<BannerDTO> banner1 = adminService.findMAIN1("MAIN1");
        List<BannerDTO> banner2 = adminService.findMAIN2("MAIN2");
        // List<BannerDTO> banner3 = adminService.findPRODUCT1("PRODUCT1");
        model.addAttribute("prodSolds", mainService.selectHitProducts());
        model.addAttribute("prodRecommend", mainService.selectRecomendProducts());
        model.addAttribute("prodRecent", mainService.selectRecentProducts());
        model.addAttribute("prodDiscount", mainService.selectDiscountProducts());

       log.info("AdminController - banner : "+banner1.toString());

        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);
        //model.addAttribute("banner3", banner3);

        return "/index";
    }

    @PostMapping("/main/index")
    public ResponseEntity<?> banner(@RequestBody Map<String, Object> map){

        String bannerNO = (String)map.get("bannerNo");

        log.info("bannerNO : "+bannerNO);

        Banner banner = adminService.findByIdBanner(bannerNO);

        log.info("조회수 올리고 난 다음 조회하기 : "+banner);

        Map<String, String> result = new HashMap<>();
        result.put("data","1");
        return ResponseEntity.ok().body(result);
    }

}
