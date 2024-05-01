package kr.co.lotte.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.dto.UserPageRequestDTO;
import kr.co.lotte.dto.UserPageResponseDTO;
import kr.co.lotte.service.AdminServiceForSangdo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminControllerForSangdo {

    private final AdminServiceForSangdo adminServiceForSangdo;

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
    public ResponseEntity<?> putGrade(@RequestBody UserDTO userDTO, HttpServletRequest req){
        return adminServiceForSangdo.updateUserGrade(userDTO);
    }

    @GetMapping("/admin/member/detail")
    public String userDetail(@RequestParam("uid")String uid, Model model){
        UserDTO userDTO = adminServiceForSangdo.selectUserForAdmin(uid);
        model.addAttribute("user", userDTO);
        return "/admin/member/detail";
    }


    @GetMapping("/admin/member/graph")
    public String memberGraph(Model model) {

        log.info("index Started");
        model.addAttribute("graphTitle", "일일 방문자 수");
        model.addAttribute("col0", "Date");
        model.addAttribute("col1", "Sales");
        model.addAttribute("col2", "Expense");
        model.addAttribute("chartData", adminServiceForSangdo.getGraphData());

        return "/admin/member/graph";

    }


}
