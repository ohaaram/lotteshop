package kr.co.lotte.controller.cs;

import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsQnaDTO;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.service.cs.CsQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
public class CsQnaController {

    private final CsQnaService csQnaService;

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
    public String qnaList(Model model, CsFaqPageRequestDTO requestDTO){
        CsFaqPageResponseDTO pageResponseDTO = csQnaService.getQnaCate1andCate2(requestDTO);
        model.addAttribute("csQna", pageResponseDTO);
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
    public String adminQnaList(Model model, CsFaqPageRequestDTO requestDTO){
        CsFaqPageResponseDTO pageResponseDTO = csQnaService.getQnaCate1andCate2(requestDTO);
        model.addAttribute("adminCsQna", pageResponseDTO);
        return "/admin/cs/qna/list";
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

}
