package kr.co.lotte.controller.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.service.cs.CsNoticeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsNoticeController {

    private static final Logger log = LoggerFactory.getLogger(CsNoticeController.class);
    @Autowired
    private final CsNoticeService csNoticeService;

    // cs.notice 전체 출력
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, CsFaqPageRequestDTO requestDTO){
        CsFaqPageResponseDTO pageResponseDTO = csNoticeService.getNoticeCate1andCate2(requestDTO);
        model.addAttribute("csNotice", pageResponseDTO);

        return "/cs/notice/list";
    }

    // cs.notice 제목 누르면 그 글로 이동
    @GetMapping("/cs/notice/view")
    public String noticeViewNo(Model model, int no){
        model.addAttribute("noticeView", csNoticeService.adminNoticeView(no));
        return "/cs/notice/view";
    }

    // admin.cs.notice.list
    @GetMapping("/admin/cs/notice/list")
    public String adminNoticeList(Model model, CsFaqPageRequestDTO requestDTO) {
        CsFaqPageResponseDTO pageResponseDTO = csNoticeService.getNoticeCate1andCate2(requestDTO);
        model.addAttribute("csNotice", pageResponseDTO);

        return "/admin/cs/notice/list";
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
    @GetMapping("/admin/cs/notice/view")
    public String adminNoticeViewNo(Model model, int no){
        log.info("no : " + no);
        log.info("야야야야야오오오오오 : " + csNoticeService.adminNoticeView(no));
        model.addAttribute("view", csNoticeService.adminNoticeView(no));
        return "/admin/cs/notice/view";
    }

    // admin.cs.notice 수정 보기
    @GetMapping("/admin/cs/notice/modify")
    public String adminNoticeModify(@RequestParam("no") int no, Model model){
        CsNoticeDTO csNoticeDTO = csNoticeService.adminNoticeView(no);
        model.addAttribute("csnotice", csNoticeDTO);
        return "/admin/cs/notice/modify";
    }

    // admin.cs.notice 수정 전송
    @PostMapping("/admin/cs/notice/modify")
    public String adminNoticeModify(@RequestParam int no, CsNoticeDTO csNoticeDTO){


        CsNoticeDTO csNoticeDTO1 = csNoticeService.adminNoticeView(no);

        // 수정할 내용
        csNoticeDTO1.setCate1(csNoticeDTO.getCate1());
        csNoticeDTO1.setTitle(csNoticeDTO.getTitle());
        csNoticeDTO1.setContent(csNoticeDTO.getContent());

        csNoticeService.adminNoticeUpdate(csNoticeDTO1);
        return "redirect:/admin/cs/notice/list";
    }

    // admin.cs.notice 목록에서 삭제
    @GetMapping("/admin/cs/notice/delete")
    public String adminNoticeDelete(int csNoticeNo){
        csNoticeService.adminNoticeDelete(csNoticeNo);
        return "redirect:/admin/cs/notice/list";
    }

}
