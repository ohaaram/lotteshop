package kr.co.lotte.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotte.dto.PolicyDTO;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.dto.UserPageRequestDTO;
import kr.co.lotte.dto.UserPageResponseDTO;
import kr.co.lotte.entity.Policy;
import kr.co.lotte.service.AdminServiceForSangdo;
import kr.co.lotte.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminControllerForSangdo {

    private final AdminServiceForSangdo adminServiceForSangdo;
    private final PolicyService policyService;


    @GetMapping("/admin/member/list")
    public String userList(Model model, UserPageRequestDTO userPageRequestDTO) {


        log.info("userList!!!");
        UserPageResponseDTO pageResponseDTO = null;
        pageResponseDTO = adminServiceForSangdo.selectsUserForAdmin(userPageRequestDTO);

        model.addAttribute(pageResponseDTO);
        //log.info(pageResponseDTO.toString());
        return "/admin/member/list";
    }

    @PutMapping("/admin/member/modifyGrade")
    public ResponseEntity<?> putGrade(@RequestBody UserDTO userDTO, HttpServletRequest req) {
        return adminServiceForSangdo.updateUserGrade(userDTO);
    }

    @GetMapping("/admin/member/detail")
    public String userDetail(@RequestParam("uid") String uid, Model model) {
        UserDTO userDTO = adminServiceForSangdo.selectUserForAdmin(uid);
        model.addAttribute("user", userDTO);
        return "/admin/member/detail";
    }


    @GetMapping("/admin/config/buyer")
    public String infoBuyer(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        Policy policy = policyService.buyerPolicy(1L);
        model.addAttribute("policy", policy);
        return "/admin/config/buyer";
    }

    @GetMapping("/admin/config/seller")
    public String infoSeller(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        Policy policy = policyService.buyerPolicy(2L);
        model.addAttribute("policy", policy);
        return "/admin/config/seller";
    }

    @GetMapping("/admin/config/finance")
    public String infoFinance(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        Policy policy = policyService.buyerPolicy(3L);
        model.addAttribute("policy", policy);
        return "/admin/config/finance";
    }

    @GetMapping("/admin/config/location")
    public String infoLocation(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        Policy policy = policyService.buyerPolicy(4L);
        model.addAttribute("policy", policy);
        return "/admin/config/location";
    }

    @GetMapping("/admin/config/privacy")
    public String infoPrivacy(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        Policy policy = policyService.buyerPolicy(5L);
        model.addAttribute("policy", policy);
        return "/admin/config/privacy";
    }

    @PostMapping("/admin/policy/modify/{id}")
    public String policyModify(@PathVariable("id") Long id, PolicyDTO policyDTO) {
        policyService.policyModify(id, policyDTO);
        if (id == 1) {
            return "redirect:/admin/config/buyer?code=100";
        } else if (id == 2) {
            return "redirect:/admin/config/seller?code=100";
        } else if (id == 3) {
            return "redirect:/admin/config/finance?code=100";
        } else if (id == 4) {
            return "redirect:/admin/config/location?code=100";
        }
        return "redirect:/admin/config/privacy?code=100";
    }
}