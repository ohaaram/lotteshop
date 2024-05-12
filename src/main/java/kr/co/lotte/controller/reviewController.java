package kr.co.lotte.controller;

import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.ReviewDTO;
import kr.co.lotte.dto.ReviewPageRequestDTO;
import kr.co.lotte.dto.ReviewPageResponseDTO;
import kr.co.lotte.entity.Review;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Controller
public class reviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final AdminService adminService;

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
    public String myReview(@RequestParam("uid")String uid, Model model, ReviewPageRequestDTO ReviewPageRequestDTO) {

        log.info("myController - myReview - uid={}", uid);

        ReviewPageResponseDTO responseDTO = reviewService.findReview(uid, ReviewPageRequestDTO);

        log.info("mycontroller - myreview - reviews={}", responseDTO.getDtoList2());

        log.info("mycontroller - myreview - responseDTO={}", responseDTO);

        model.addAttribute("reviews", responseDTO);

        //배너 출력
        List<BannerDTO> banner5 = adminService.validateBanner("MY1");
        log.info("banner5: {}", banner5);
        model.addAttribute("banner5", banner5);

        return "/my/review";
    }

    //옵션값들을 가지고 오기
    @GetMapping("/review/{itemno}")
    public ResponseEntity<?> optionName(@PathVariable("itemno")int itemno){

       String option = reviewService.findOption(itemno);

        Map<String , String> map1 = new HashMap<>();
        map1.put("option",option);
        return ResponseEntity.ok().body(map1);
    }

    //리뷰는 한번만 쓰게 하기!(내가 시킨 하나의 상품당 하나의 리뷰)
    @GetMapping("/review/{orderno}/{prodno}/{itemno}")
    public ResponseEntity<Map<String, Integer>> rCheck(@PathVariable("orderno") int orderno, @PathVariable("prodno")int prodno, @PathVariable("itemno")int itemno, Model model){

        log.info("review- controller - orderno : "+orderno);
        log.info("review- controller - prodno : "+prodno);
        log.info("review- controller - itemno : "+itemno);

        int count = reviewService.findByorderno(orderno,prodno,itemno);

        log.info("review - controller - result : "+count);

        Map<String , Integer> map1 = new HashMap<>();
        map1.put("count",count);
        return ResponseEntity.ok().body(map1);
    }
}