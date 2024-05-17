package kr.co.lotte.controller.cs;

import kr.co.lotte.dto.*;
import kr.co.lotte.entity.ProductQna;
import kr.co.lotte.security.MyManagerDetails;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.ProductQnaService;
import kr.co.lotte.service.cs.CsQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class CsQnaController {

    private final CsQnaService csQnaService;
    private final ProductQnaService productQnaService;

    // cs.qna reg
    @GetMapping("/cs/qna/write")
    public String qnaWrite(){
        return "/cs/qna/write";
    }

    // qna 글쓰기
    @PostMapping("/cs/qna/write")
    public String qnaWrite(CsQnaDTO csQnaDTO ){
        csQnaService.insertQna(csQnaDTO);
        return "redirect:/cs/qna/list";
    }

    // cs.qna 전체 출력
    @GetMapping("/cs/qna/list")
    public String qnaList(Model model, CsFaqPageRequestDTO requestDTO, ProductQnaDTO productQnaDTO){
        CsFaqPageResponseDTO pageResponseDTO = csQnaService.getQnaCate1andCate2(requestDTO);
        List<ProductQna> prodQnaList = productQnaService.productQnas();
        model.addAttribute("csQna", pageResponseDTO);
        model.addAttribute("prodQnaList", prodQnaList);

        return "/cs/qna/list";
    }

    // cs.qna view
    @GetMapping("/cs/qna/view")
    public String qnaView(Model model, int no){
        model.addAttribute("qnaView",csQnaService.qnaView(no));
        return "/cs/qna/view";
    }

    // admin.cs.qna 전체 출력
    @GetMapping("/admin/cs/qna/list")
    public String adminQnaList(Model model, CsFaqPageRequestDTO requestDTO, CsQnaDTO csQnaDTO, ProductQnaDTO productQnaDTO , Authentication authentication){

        CsFaqPageResponseDTO pageResponseDTO = null;
        try {
            //여기는 관리자
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            if (requestDTO.getGroup() == null || requestDTO.getGroup() == "" || requestDTO.getGroup().equals("qna")) {
                requestDTO.setGroup("qna");
                pageResponseDTO = csQnaService.getQnaCate1andCate2(requestDTO);
            } else {
                requestDTO.setGroup("product");
                pageResponseDTO = productQnaService.getProdQnaCate(requestDTO);
            }

        }catch (Exception e){
            //여기는 매니저
            MyManagerDetails myManagerDetails = (MyManagerDetails) authentication.getPrincipal();
            requestDTO.setGroup("product");
            pageResponseDTO = productQnaService.getProdQnaCate(requestDTO, myManagerDetails.getUser().getSellerUid());
        }
        model.addAttribute("adminCsQna", pageResponseDTO);
        return "/admin/cs/qna/list";
    }

    // admin.cs.qna 전체 출력
    @GetMapping("/admin/cs/qna/sellerList")
    public String sellerQnaList(Model model, CsFaqPageRequestDTO requestDTO){
        CsFaqPageResponseDTO pageResponseDTO1 = productQnaService.getProdQnaCate(requestDTO);
        model.addAttribute("adminProdQna", pageResponseDTO1);
        return "/admin/cs/qna/sellerList";
    }

    // admin.cs.qna reply
    @GetMapping("/admin/cs/qna/reply")
    public String adminQnaReply(@RequestParam("no") int no, Model model){

        CsQnaDTO csQnaDTO = csQnaService.qnaView(no);
        model.addAttribute("adminCsQna", csQnaDTO);

        return "/admin/cs/qna/reply";
    }
    @PostMapping("/admin/cs/qna/reply")
    public String adminQnaReply(@RequestParam int no, CsQnaDTO csQnaDTO) {

        CsQnaDTO qnaViewDTO = csQnaService.qnaView(no);

        // 답변 내용 설정
        qnaViewDTO.setComment(csQnaDTO.getComment());

        if(!csQnaDTO.getComment().isEmpty()){
            qnaViewDTO.setStatus("답변 완료");
            // 답변 업데이트
            csQnaService.adminQnaComment(qnaViewDTO);
        }

        return "redirect:/admin/cs/qna/list";
    }

    @GetMapping("/admin/cs/qna/reply2")
    public String adminQnaReply2(@RequestParam("no") int no, Model model){

        ProductQnaDTO productQnaDTO = productQnaService.prodQnaView(no);
        model.addAttribute("adminProdQna", productQnaDTO);

        return "/admin/cs/qna/reply2";
    }

    @PostMapping("/admin/cs/qna/reply2")
    public String adminQnaReply2(@RequestParam int no, ProductQnaDTO productQnaDTO) {

        ProductQnaDTO prodQnaView = productQnaService.prodQnaView(no);

        // 답변 내용 설정
        prodQnaView.setAnswer(productQnaDTO.getAnswer());

        if(!productQnaDTO.getAnswer().isEmpty()){
            prodQnaView.setStatus1("답변 완료");
            // 답변 업데이트
            productQnaService.adminProdQnaAnswer(prodQnaView);
        }

        return "redirect:/admin/cs/qna/list";
    }

    @PostMapping("/product/view/qna")
    public ResponseEntity<String> productQnaWrite(@RequestBody ProductQna productQna){


        log.info("들어오나요");

        log.info("productQna : "+productQna);
        // 상품 문의 생성
        ProductQna savedProductQna = csQnaService.writeProdQna(productQna);
        if(savedProductQna != null){
            log.info(savedProductQna.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body("상품 문의가 성공적으로 등록 되었습니다.");
        }else{
            log.info("정보저장되나요".toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 문의 등록에 실패 하였습니다.");
        }
    }



    @GetMapping("/admin/cs/qna/delete")
    public String delete(@RequestParam(name = "prodQnaNo") int prodQnaNo,@RequestParam(name = "type")  int type){

        log.info("prodQnaNo :"+prodQnaNo);
        if(type == 1){
            csQnaService.delete2(prodQnaNo);
        }else{
            csQnaService.delete(prodQnaNo);
        }


        return "redirect:/admin/cs/qna/list";
    }

}
