package kr.co.lotte.controller.cs;

import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotte.service.cs.CsQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class CsQnaController {

    private final CsQnaService csQnaService;

    /*
    @GetMapping("/admin/cs/qna/list")
    public String adminQnaList() {

        return "/admin/cs/qna/list";
    }
    
     */
    
    // cs.qna 목록
    @GetMapping("/cs/qna/list")
    public String csQna(){

        return "/cs/qna/list";
    }
    
}
