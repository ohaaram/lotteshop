package kr.co.lotte.controller.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.service.cs.CsNoticeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsNoticeController {

    private static final Logger log = LoggerFactory.getLogger(CsNoticeController.class);
    private final CsNoticeService csNoticeService;

    // cs.notice.list
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, @RequestParam(name="cate1", required = false)String cate1) {
        if(cate1 != null) {
            cate1="customer";
        }
        Map<String, List<CsNoticeDTO>> dtoLists = csNoticeService.getCsNoticeList(cate1);
        log.info("dtoLists.size() : " + dtoLists.size());
        model.addAttribute("dtoLists", dtoLists);
        return "/cs/notice/list";
    }

    // admin.cs.notice.list
    @GetMapping("/admin/cs/notice/list")
    public String adminNoticeList(Model model, CsFaqPageRequestDTO requestDTO) {
        CsFaqPageResponseDTO pageResponseDTO = csNoticeService.getNoticeCate1and2(requestDTO);
        model.addAttribute("csNotice", pageResponseDTO);

        return "/admin/cs/notice/list";
    }

    // admin notice 뷰 페이지
    @GetMapping("/admin/cs/notice/view")
    public String adminNoticeView(){

        return "/admin/cs/notice/view";
    }
    //admin.cs.notice.reg 폼 불러오기
    @GetMapping("/admin/cs/notice/register")
    public String noticeWrite(){
        return "/admin/cs/notice/register";
    }

    //admin.cs.notice register
    @PostMapping("/admin/cs/notice/register")
    public String insertNotice(CsNoticeDTO csNoticeDTO){
        csNoticeService.insertCsNotice(csNoticeDTO);

        return "redirect:/admin/cs/notice/list";
    }

    // admin.cs.notice 글 번호로 보기

    /*
    // admin.cs.notice 수정 폼 불러와
    @GetMapping("/admin/cs/notice/modify")
    public String adminNoticeModify(@RequestParam("csNoticeNo") int csNoticeNo, Model model){
        CsNoticeDTO csNoticeDTO = csNoticeService.adminNoticeUpdate();
        model.addAttribute("csNotice", csNoticeDTO);
        return "/admin/cs/notice/modify";
    }

     */



}
