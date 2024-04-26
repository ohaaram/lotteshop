package kr.co.lotte.controller.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.service.cs.CsNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsNoticeController {

    private final CsNoticeService csNoticeService;


    // admin notice 리스트 출력
    @GetMapping("/admin/cs/notice/list")
    public String adminNoticeList(){

        return "/admin/cs/notice/list";
    }
    // cs notice 리스트 출력
    @GetMapping("/cs/notice/list")
    public String noticeList(){

        return "/cs/notice/list";
    }

    // admin notice 뷰 페이지
    @GetMapping("/admin/cs/notice/view")
    public String adminNoticeView(){

        return "/admin/cs/notice/view";
    }

    //admin 페이지 공지사항 작성
    @GetMapping("/admin/cs/notice/register")
    public String noticeWrite(){
        return "/admin/cs/notice/register";
    }

    @PostMapping("/admin/cs/notice/write")
    public String insertNotice(Model model, CsNoticeDTO csNoticeDTO){
        csNoticeService.insertCsNotice(csNoticeDTO);

        return "redirect:/admin/cs/notice/write?code=100";
    }

}
