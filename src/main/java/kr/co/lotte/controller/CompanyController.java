package kr.co.lotte.controller;


import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.Media;
import kr.co.lotte.entity.Recruit;
import kr.co.lotte.service.BlogService;
import kr.co.lotte.service.MediaService;
import kr.co.lotte.service.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final BlogService blogService;
    private final RecruitService recruitService;
    private final MediaService mediaService;


    @GetMapping("/company/index")
    public String index(Model model) {
        //blog에 있는 전체 글을 출력

        List<Blog> list = blogService.list();//블로그에 있는 모든 글을 가지고 옴 근데 나는 title만 필요하지?

        log.info("index-list : " + list);

        model.addAttribute("list", list);

        return "/company/index";
    }


    @GetMapping("/company/culture")
    public String culture() {


        return "/company/culture";
    }


    @GetMapping("/company/view")
    public String view(Model model, int bno) {

        BlogDTO blogDTO = blogService.findById(bno);

        log.info("blogDTO : " + blogDTO);

        //번호 받아서 조회
        model.addAttribute("blogs", blogDTO);

        return "/company/view";
    }

    //채용정보
    @GetMapping("/company/recruit")
    public String recruit(Model model) {

        log.info("컨트롤러에는 들어오나요?");

        List<Recruit> list = recruitService.findAll();//등록되어있는 모든 채용정보 가지고 오기

        model.addAttribute("list", list);

        return "/company/recruit";
    }


    //채용정보 내용
    @GetMapping("/company/recruitContent")
    public String recruitContent(Model model, int r_no) {

        Recruit recruit_content = recruitService.findById(r_no);//등록되어있는 모든 채용정보 가지고 오기

        log.info("controller - recruit_content : " + recruit_content);

        model.addAttribute("content", recruit_content);

        return "/company/recruitContent";

    }

    //소식과 이야기 페이지 띄우기
    @GetMapping("/company/story/{cate}")
    public String story(Model model, @PathVariable("cate") String cate) {

        log.info("cate 값 : " + cate);

        log.info("controller - story 여기까지 들어옴? ");


        List<Blog> blogs = blogService.selectBlogForStory(cate);//카테고리에 따른 블로그 내용


        model.addAttribute("card", blogs);

        return "/company/story";
    }


    //소식과 이야기 페이지 기본페이지 띄우기
    @GetMapping("/company/story")
    public String story(Model model) {
        List<Blog> blogs = blogService.findAll();
        model.addAttribute("card", blogs);
        return "/company/story";
    }

    //영상 띄우기
    @GetMapping("/company/media")
    public String media(Model model){

        List<Media> media = mediaService.findAll();

        model.addAttribute("media", media);

        return "/company/media";
    }


}
