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
import kr.co.lotte.service.MainService;
import kr.co.lotte.service.MarketService;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final MainService mainService;


    @GetMapping("/product/list")
    public String list(Model model, MainProductsPageRequestDTO requestDTO){
        model.addAttribute("pageResponseDTO", mainService.searchListProducts(requestDTO));
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

    @ResponseBody
    @PostMapping("/product/order")
    public ResponseEntity order(@RequestBody Map<String , Object> map, HttpSession session){
        List<Integer> nos = (List<Integer>) map.get("itemsNos");
        List<Integer> counts = (List<Integer>) map.get("itemsCounts");
        session.setAttribute("nos", nos);
        session.setAttribute("counts", counts);

        Map<String , String> map1 = new HashMap<>();
        map1.put("result","success");
        return ResponseEntity.ok().body(map1);
    }

    //바로구매(장바구니 안 거치고)
    @GetMapping("/product/order")//
    public String order( Model model, HttpSession session, Authentication authentication){
        int status = 0;
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        List<Integer> nos = (List<Integer>) session.getAttribute("nos");
        List<Integer> counts = (List<Integer>) session.getAttribute("counts");
        log.info("nos nonono~ : "+nos);
        model.addAttribute("subProducts", marketService.selectProducts(nos));
        model.addAttribute("counts", counts);
        model.addAttribute("status", status);
        return "/product/order";
    }

    //바로구매(장바구니 거치고)
    @GetMapping("/product/order2")//
    public String order2( Model model, Authentication authentication, @RequestParam(name = "list") List<Integer> list){
        log.info("list : "+list);
        int status = 1;
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        List<Carts> cartsList = marketService.selectCarts(list);

        List<Integer> counts = cartsList.stream().map(item ->   item.getCartProdCount()).toList();
        List<Integer> nos = cartsList.stream().map(item ->   item.getProdNo()).toList();
        model.addAttribute("cartlist", cartsList);
        model.addAttribute("subProducts", marketService.selectProducts(nos));
        model.addAttribute("counts", counts);
        model.addAttribute("status", status);
        return "/product/order";
    }

    //구매하기 (orderTable  포인트 사용 감소)
    @ResponseBody
    @PostMapping("/product/orderBuy")
    public ResponseEntity orderBuy(@RequestBody OrdersDTO ordersDTO){
        log.info("이거확인 "+ordersDTO.toString());
        return marketService.insertOrderAndPoint(ordersDTO);
    }

    //구매하기2( 카트제거 , orderItems 넣기)
    @ResponseBody
    @PostMapping("/product/deleteCartForOrder")
    public ResponseEntity deleteCartForOrder(@RequestBody Map<String, List<Integer>> map){
        List<Integer> list = map.get("lists");
        return marketService.deleteCartForBuy(list);
    }
    
    //구매하기2-1 (세션제거, orderItems 넣기)
    @ResponseBody
    @GetMapping("/product/insertItems")
    public ResponseEntity insertItems(Model model, HttpSession session,
                                      @RequestParam(name = "orderNo")int orderNo,Authentication authentication){
        List<Integer> nos = (List<Integer>) session.getAttribute("nos");
        List<Integer> counts = (List<Integer>) session.getAttribute("counts");
        session.removeAttribute("nos");
        session.removeAttribute("counts");

        return marketService.insertItemsForBuy(nos ,counts , orderNo);
    }


    //주문 확인시 product 재고 감소 + 포인트 주고 + usertotalPrice에 넣기
    
    //주문 취소시 사용한 포인트 돌려주기 + userTotalPrice 빼기

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
