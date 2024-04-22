package kr.co.lotte.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private AdminService adminService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model, HttpSession session){

        
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

        //customUserDetails 가 저장된 SecurityContextHolder 호출
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        model.addAttribute("uid", uid);
        model.addAttribute("role", role);
        return "/index";
    }
}
