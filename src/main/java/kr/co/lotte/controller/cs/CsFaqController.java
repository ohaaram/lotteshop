package kr.co.lotte.controller.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.service.cs.CsFaqService;
import kr.co.lotte.service.cs.CsNoticeService;
import kr.co.lotte.service.cs.CsQnaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CsFaqController {

    private static final Logger log = LoggerFactory.getLogger(CsFaqController.class);
    private final CsFaqService csFaqService;
    private final CsNoticeService csNoticeService;
    private final CsQnaService csQnaService;



    @GetMapping(value = {"/cs","/cs/index"} )
    public String index(Model model){

        model.addAttribute("csNotice", csNoticeService.noticeList());
        model.addAttribute("csQna", csQnaService.qnaList());

        return "/cs/index";
    }


    // admin.faq.list 출력
    @GetMapping("/admin/cs/faq/list")
    public String adminFaqList(Model model, CsFaqPageRequestDTO requestDTO){
        CsFaqPageResponseDTO pageResponseDTO = csFaqService.getFaqsCate1and2(requestDTO);
        model.addAttribute("csFaqs", pageResponseDTO);

        return "/admin/cs/faq/list";
    }


    // admin.faq.list 특정 게시글 출력
    /*
    @GetMapping("/admin/cs/faq/list")
    @ResponseBody
    public List<CsFaq> getFaqsCate1and2(@RequestParam("cate1")String cate1,
                                        @RequestParam("cate2")String cate2){
        log.info("cate1 나와라");
        log.info("cate2 나와라");
        return csFaqService.getFaqsCate1and2(cate1, cate2);
    }
     */


    // admin.cs.faq 수정 폼 출력
    @GetMapping("/admin/cs/faq/{no}")
    public String adminFaqModify(@PathVariable int no, Model model){

        CsFaqDTO csFaqDTO = csFaqService.faqSelectNo(no);
        model.addAttribute("csFaqDTO", csFaqDTO);
        return "/admin/cs/faq/modify";
    }

    // admin.cs.faq 수정하기
    @PostMapping("/admin/cs/faq")
    public String adminFaqUpdate(CsFaqDTO csFaqDTO){
        csFaqService.adminFaqUpdate(csFaqDTO);
        return "redirect:/admin/cs/faq/list";
    }

    // admin.cs.faq 삭제하기
    @GetMapping("/admin/cs/faq/delete/{no}")
    public String adminFaqDelete(@PathVariable int no){
        csFaqService.adminFaqDelete(no);
        return "redirect:/admin/cs/faq/list";
    }

    // admin.cs.faq 선택삭제
    @PostMapping("/admin/cs/faq/delete/selectDelete")
    public String adminFaqDeleteSelected(@RequestParam("selectedNo") List<Integer> selectedNo){
        csFaqService.adminFaqDeleteSelected(selectedNo);
        return "redirect:/admin/cs/faq/list";
    }

    // admin.cs.faq 글 작성 페이지
    @GetMapping("/admin/cs/faq/register")
    public String adminFaqRegister(Model model){
        CsFaqDTO csFaqDTO = new CsFaqDTO();
        model.addAttribute("csFaqDTO", csFaqDTO);
        return "/admin/cs/faq/register";
    }

    // admin.cs.faq 글 작성
    @PostMapping("/admin/cs/faq/register")
    public String adminFaqWrite(CsFaqDTO csFaqDTO){
        log.info(csFaqDTO.toString()+"아아아아아");
        csFaqService.adminFaqWrite(csFaqDTO);
        return "redirect:/admin/cs/faq/list";
    }

    // cs.faq 출력
    @GetMapping("/cs/faq")
    public String faqList(Model model, @RequestParam(name = "cate1" , required = false) String cate1) {
        if(cate1 ==null){
            cate1="user";
        }
        List<List<CsFaqDTO>> dtoLists = csFaqService.getFaqArticles(cate1);
        log.info("dtoLists.size() : " + dtoLists.size());
        model.addAttribute("dtoLists", dtoLists);
        return "/cs/faq/user";
    }

    // cs.faq.view 출력
    @GetMapping("/cs/faq/view/{no}")
    public String faqView(@PathVariable int no, Model model){

        CsFaqDTO csFaqDTO = csFaqService.faqSelectNo(no);
        model.addAttribute("csFaqDTO", csFaqDTO);
        return "/cs/faq/view";
    }

    // admin.faq. 특정 글 조회
    @GetMapping("/admin/cs/faq/view/{no}")
    public String adminFaqView(@PathVariable int no, Model model){

        CsFaqDTO csFaqDTO = csFaqService.faqSelectNo(no);
        model.addAttribute("csFaqDTO", csFaqDTO);
        return "/admin/cs/faq/view";
    }

}
