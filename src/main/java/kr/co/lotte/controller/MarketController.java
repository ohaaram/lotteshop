package kr.co.lotte.controller;


import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.Carts;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.CartsRepository;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.MarketService;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MarketController {

    private final MarketService marketService;
    private final MemberService memberService;
    private final ReviewService reviewService;


    @GetMapping("/product/list")
    public String list(){

        return "/product/list";
    }

    @GetMapping("/product/view")
    public String view(Model model, @RequestParam(name = "prodno") int prodno, ReviewPageRequestDTO reviewPageRequestDTO){

        log.info("prodno 값 : "+prodno);

        //상품 조회
        ProductsDTO productsDTO = marketService.selectProduct(prodno);

        //subProducts에서 prodno로 조회, color과 size의 리스트를 들고 온다
        List<SubProducts> Options = marketService.findAllByProdNo(prodno);

        log.info("Options : "+Options.size());

        log.info("options : "+Options);

        log.info("view - getMapping - productsDTO : "+productsDTO);

        log.info("/product/view : 여기까지 들어오는건가?");

        model.addAttribute("options", Options);


        //리뷰 조회
        ReviewPageResponseDTO reviewPageResponseDTO = reviewService.selectReviews(prodno,reviewPageRequestDTO);



        //리뷰 별점 - 평균, 비율 구하기
        ReviewRatioDTO reviewRatioDTO = reviewService.selectForRatio(prodno);

        model.addAttribute("product", productsDTO);//제품정보와 이미지정보도 같이 담은 productsDTO
        model.addAttribute("reviewPage", reviewPageResponseDTO);
        model.addAttribute(reviewRatioDTO);

        return "/product/view";
    }

    //장바구니 넣기
    @PostMapping("/product/view")
    public ResponseEntity<Map<String, String>> view(@RequestBody Map<String , Object> map){
            log.info(map.get("itemsCounts").toString());
            log.info(map.get("itemsNos").toString());
            List<Integer> nos = (List<Integer>) map.get("itemsNos");
            List<Integer> counts = (List<Integer>) map.get("itemsCounts");
            String uid = map.get("userId").toString();
            return marketService.inserCart(uid, counts , nos);
    };


    @GetMapping("/product/order")//여기는 바로 구매 했을 때 넘겨주는 페이지(세션으로 넘김)
    public String order(HttpServletRequest request, Model model){

        //상품에 대한 이미지가 있어야하네...

        HttpSession session = request.getSession();

        List<Map<String, Object>> ordersDTOS = (List<Map<String, Object>>) session.getAttribute("orderList");

        ProductsDTO products= (ProductsDTO) session.getAttribute("productsDTO");

        log.info("ordersDTOS : "+ordersDTOS);


        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        model.addAttribute("product", products);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("ordersDTOS", ordersDTOS);

        return "/product/order";
    }

    //옵션관련된것
    @ResponseBody
    @GetMapping("/product/optionSelect")
    public ResponseEntity optionSelect(@RequestParam(name = "color") String color,
                                       @RequestParam(name = "prodNo") int prodNo){

        return  marketService.selectOption(color, prodNo);
    }

    @GetMapping("/product/cart")
    public String cart(Authentication authentication , Model model){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<Carts> carts = marketService.selectCart(user.getUid());
        List<Integer> subProdnos = carts.stream().map(e -> e.getProdNo()).toList();
        model.addAttribute("subProducts", marketService.selectProducts(subProdnos));
        model.addAttribute("carts", carts);
        return "/product/cart";

    }

    //장바구니 삭제
    @ResponseBody
    @PutMapping("/product/cart/delete")
    public ResponseEntity deleteCart(@RequestBody  Map<String,List<Integer>> map){
        log.info(map.toString());
        return marketService.deleteCart(map.get("list"));

    }

    @GetMapping("/product/search")
    public String search(){

        return "/product/search";
    }

    @GetMapping("/product/complete")
    public String complete(){

        return "/product/complete";
    }




}
