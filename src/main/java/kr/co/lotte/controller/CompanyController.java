package kr.co.lotte.controller;


import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.AlgorithmConstraints;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final BlogService blogService;



    @GetMapping("/company/index")
    public String index(Model model){
        //blog에 있는 전체 글을 출력

        List<Blog> list = blogService.list();//블로그에 있는 모든 글을 가지고 옴 근데 나는 title만 필요하지?

        log.info("index-list : "+list);

        model.addAttribute("list",list);

        return "/company/index";
    }



    @GetMapping("/company/culture")
    public String culture(){

        return "/company/culture";
    }


    @GetMapping("/company/view")
    public String view(Model model,int bno){

         BlogDTO blogDTO = blogService.findById(bno);

        log.info("blogDTO : "+blogDTO);
         
         //번호 받아서 조회
         model.addAttribute("blogs",blogDTO);

        return "/company/view";
    }

    @GetMapping("/company/recruit")
    public String recruit(){



        return "/company/recruit";
    }
}
