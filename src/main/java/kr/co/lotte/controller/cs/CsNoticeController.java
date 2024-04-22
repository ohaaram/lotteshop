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


    @GetMapping("/admin/cs/notice/list")
    public String adminNoticeList(){

        return "/admin/cs/notice/list";
    }

    @GetMapping("/admin/cs/notice/view")
    public String adminNoticeView(){

        return "/admin/cs/notice/view";
    }

    @GetMapping("/admin/cs/notice/write")
    public String adminNoticeWrite(){
        return "/admin/cs/notice/write";
    }

    @PostMapping("/admin/cs/notice/write")
    public String insertNotice(Model model, CsNoticeDTO csNoticeDTO){
        csNoticeService.insertCsNotice(csNoticeDTO);

        return "redirect:/admin/cs/notice/write?code=100";
    }

    @GetMapping("/admin/cs/qna/list")
    public String adminQnaList(){

        return "/admin/cs/qna/list";
    }

}
