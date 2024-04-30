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

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminControllerForSangdo {

    private final AdminServiceForSangdo adminServiceForSangdo;
/*
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

 */
}
