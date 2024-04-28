package kr.co.lotte.controller;


import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.Recruit;
import kr.co.lotte.service.BlogService;
import kr.co.lotte.service.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final BlogService blogService;
    private final RecruitService recruitService;



    @GetMapping("/company/index")
    public String index(Model model){
        //blog에 있는 전체 글을 출력

        List<Blog> list = blogService.list();//블로그에 있는 모든 글을 가지고 옴

        log.info("index-list : "+list);

        model.addAttribute("list",list);

        return "/company/index";
    }


    //기업문화 뷰
    @GetMapping("/company/culture")
    public String culture(){

        return "/company/culture";
    }

    //index에서 블로그 제목이나 화살표를 누르면 블로그 페이지로 이동
    @GetMapping("/company/blogView")
    public String view(Model model,int bno){

         BlogDTO blogDTO = blogService.findById(bno);

        log.info("blogDTO : "+blogDTO);
         
         //번호 받아서 조회
         model.addAttribute("blogs",blogDTO);

        return "/company/blogView";
    }

    //채용정보 
    @GetMapping("/company/recruit")
    public String recruit(Model model){

        log.info("컨트롤러에는 들어오나요?");

        List<Recruit> list = recruitService.findAll();//등록되어있는 모든 채용정보 가지고 오기

        model.addAttribute("list",list);

        return "/company/recruit";
    }


    @GetMapping("/company/recruitContent")
    public String recruitContent(Model model,int r_no){

        Recruit recruit_content= recruitService.findById(r_no);//등록되어있는 모든 채용정보 가지고 오기

        log.info("controller - recruit_content : "+recruit_content);

        model.addAttribute("content",recruit_content);

        return "/company/recruitContent";

    }
}
