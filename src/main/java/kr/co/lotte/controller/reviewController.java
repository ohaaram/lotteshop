package kr.co.lotte.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotte.dto.ReviewDTO;
import kr.co.lotte.entity.Review;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class reviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    // 리뷰 쓰기 - Form 데이터 받아서 처리
    @PostMapping("/review")
    public String reviewWrite(@RequestParam("thumb") MultipartFile thumb, ReviewDTO reviewDTO) throws IOException {
        log.info("reviewWrite Cont 1 : " + reviewDTO.getUid());
        log.info("reviewWrite Cont 2 : " + reviewDTO.getScore());
        log.info("reviewWrite Cont 3 : " + thumb);
        // 이미지 리사이징은 서비스에서 함
        // 리뷰 등록 service
        reviewService.insertReview(reviewDTO, thumb);
        return "redirect:/market/orderList?uid="+reviewDTO.getUid();
    }

    //리뷰 등록
    @ResponseBody
    @PostMapping("/my/order/write_review")
    public void write_review(ReviewDTO reviewDTO, HttpServletResponse response)throws IOException {

        log.info("여기 들어와지는고니? 여기는 mycontroller");

        log.info("mycontroller - write_review - reviewDTO={}", reviewDTO);

        Object data = reviewService.rRegister(reviewDTO);

        log.info(data.toString());

        response.sendRedirect("/lotteshop/my/review?uid=" + reviewDTO.getUid());
    }

    //내가 작성했던 리뷰들 다 보여주기
    @GetMapping("/my/review")
    public String myReview(@RequestParam("uid")String uid, Model model) {

        log.info("myController - myReview - uid={}", uid);

        List<Review> reviews = reviewService.findReview(uid);

        log.info("mycontroller - myreview - reviews={}", reviews);

        model.addAttribute("reviews", reviews);

        return "/my/review";
    }

}
