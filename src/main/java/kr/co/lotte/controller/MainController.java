package kr.co.lotte.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.entity.Banner;
import kr.co.lotte.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = {"/", "/index"})
    public String index(Model model, HttpSession session){

        List<BannerDTO> banner1 = adminService.findMAIN1("MAIN1");
        List<BannerDTO> banner2 = adminService.findMAIN2("MAIN2");
        // List<BannerDTO> banner3 = adminService.findPRODUCT1("PRODUCT1");



        log.info("AdminController - banner : "+banner1.toString());

        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);
        //model.addAttribute("banner3", banner3);

        
        //배너
        if (session != null) {// 세션이 존재하는지 확인합니다.
            // 세션에서 bannerDTO 속성을 가져옵니다.
            BannerDTO bannerDTO = (BannerDTO) session.getAttribute("bannerDTO");

            // bannerDTO가 null이 아니면 세션에 해당 속성이 존재한다는 것이므로 이후 코드를 실행합니다.
            if (bannerDTO != null) {
                model.addAttribute("bannerDTO", bannerDTO);
                log.info("MainController - index : bannerDTO " + bannerDTO);
            }
        } else {
            log.error("세션이 존재하지 않습니다.");
        }

        return "/index";
    }

    @PostMapping("/main/index")
    public ResponseEntity<?> banner(@RequestBody Map<String, Object> map){
        String link = (String)map.get("link");
        String bannerNO = (String)map.get("bannerNo");

        log.info("link : "+link);
        log.info("bannerNO : "+bannerNO);


        Banner banner = adminService.findByIdBanner(bannerNO);

        log.info("조회수 올리고 난 다음 조회하기 : "+banner);

        Map<String, String> result = new HashMap<>();
        result.put("data","1");
        return ResponseEntity.ok().body(result);
    }
}
