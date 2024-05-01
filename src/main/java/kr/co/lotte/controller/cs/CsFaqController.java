package kr.co.lotte.controller.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.service.cs.CsFaqService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping(value = {"/cs","/cs/index"} )
    public String index(){
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

    /*
    // faq.trade 페이지 출력
    @GetMapping("/cs/faq/trade")
    public String faqTrade(Model model) {

        List<CsFaq> etcord = csFaqService.getEtcOrdArticle();
        model.addAttribute("etcord", etcord);

        List<CsFaq> etccard = csFaqService.getEtcCardArticle();
        model.addAttribute("etccard", etccard);

        List<CsFaq> cashreceipt = csFaqService.getCashReceiptArticle();
        model.addAttribute("cashreceipt", cashreceipt);

        List<CsFaq> taxreceipt = csFaqService.getTaxReceiptArticle();
        model.addAttribute("taxreceipt", taxreceipt);

        return "/cs/faq/trade";
    }

    // faq.order 페이지 출력
    @GetMapping("/cs/faq/order")
    public String faqOrder(Model model) {

        List<CsFaq> lpay = csFaqService.getLpayArticle();
        model.addAttribute("lpay", lpay);

        List<CsFaq> etc = csFaqService.getEtcArticle();
        model.addAttribute("etc", etc);

        List<CsFaq> mutong = csFaqService.getMutongArticle();
        model.addAttribute("mutong", mutong);

        List<CsFaq> ord = csFaqService.getOrdArticle();
        model.addAttribute("ord", ord);

        List<CsFaq> ordlist = csFaqService.getOrdlistArticle();
        model.addAttribute("ordlist", ordlist);

        List<CsFaq> card = csFaqService.getCardArticle();
        model.addAttribute("card", card);

        return "/cs/faq/order";
    }

    // faq.delivery 페이지 출력
    @GetMapping("/cs/faq/delivery")
    public String faqDelivery(Model model) {

        List<CsFaq> buy = csFaqService.getBuyArticle();
        model.addAttribute("buy", buy);

        List<CsFaq> delp = csFaqService.getDelpArticle();
        model.addAttribute("delp", delp);

        List<CsFaq> delm = csFaqService.getDelmArticle();
        model.addAttribute("delm", delm);

        List<CsFaq> delinfo = csFaqService.getDelinfoArticle();
        model.addAttribute("delinfo", delinfo);

        List<CsFaq> gift = csFaqService.getGiftArticle();
        model.addAttribute("gift", gift);

        return "/cs/faq/delivery";
    }

    // faq.cancel 페이지 출력
    @GetMapping("/cs/faq/cancel")
    public String faqCancel(Model model) {

        List<CsFaq> ordCancel = csFaqService.getOrdCancelArticle();
        model.addAttribute("ordCancel", ordCancel);

        List<CsFaq> refund = csFaqService.getRefundArticle();
        model.addAttribute("refund", refund);

        List<CsFaq> as = csFaqService.getAsArticle();
        model.addAttribute("as", as);

        List<CsFaq> asp = csFaqService.getAspArticle();
        model.addAttribute("asp", asp);

        List<CsFaq> change = csFaqService.getChangeArticle();
        model.addAttribute("change", change);

        List<CsFaq> returns = csFaqService.getReturnsArticle();
        model.addAttribute("returns", returns);

        return "/cs/faq/cancel";
    }

    // faq.eventCupon 페이지 출력
    @GetMapping("/cs/faq/eventCupon")
    public String faqEventCupon(Model model) {

        List<CsFaq> lpoint = csFaqService.getLpointArticles();
        model.addAttribute("lpoint", lpoint);

        List<CsFaq> lstamp = csFaqService.getLstampArticles();
        model.addAttribute("lstamp", lstamp);

        List<CsFaq> review = csFaqService.getReviewArticles();
        model.addAttribute("review", review);

        List<CsFaq> onmile = csFaqService.getOnmileArticles();
        model.addAttribute("onmile", onmile);

        List<CsFaq> event = csFaqService.getEventArticles();
        model.addAttribute("event", event);

        return "/cs/faq/eventCupon";
    }
    */

}
