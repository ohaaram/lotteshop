package kr.co.lotte.controller;


import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.CartsRepository;
import kr.co.lotte.repository.OrderItemsRepository;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final AdminService adminService;
    private final ProductQnaService productQnaService;

    @Autowired
    private final MainService mainService;
    @Autowired
    private OrderItemsRepository orderItemsRepository;


    @GetMapping("/product/list")
    public String list(Model model, MainProductsPageRequestDTO requestDTO, @AuthenticationPrincipal MyUserDetails userDetails) {
        int view = 0;

        log.info("requestDTO의 seller에 뭐가 있는지 확인 : "+requestDTO.getSeller());

    if(requestDTO.getSeller() != "" && requestDTO.getSeller() != null){
            MainProductsPageResponseDTO pageResponseDTO = mainService.searchListProductsForSeller(requestDTO);
            List<Products> products = pageResponseDTO.getDtoList();
            try {
                User user = userDetails.getUser();
                List<Products> newProducts = mainService.hahaha(products, user.getUid());
                pageResponseDTO.setDtoList(newProducts);
            } catch (Exception e) {
                for (Products p : products) {
                    p.setLikeState(0);
                }
                pageResponseDTO.setDtoList(products);
            }
            model.addAttribute("view", view);
            model.addAttribute("pageResponseDTO", pageResponseDTO);
            return "/product/list2";


    }    else if(requestDTO.getBoard() == "" || requestDTO.getBoard() == null){
        MainProductsPageResponseDTO pageResponseDTO = mainService.searchListProducts(requestDTO);
        List<Products> products = pageResponseDTO.getDtoList();
        try {
            User user = userDetails.getUser();
            List<Products> newProducts = mainService.hahaha(products, user.getUid());
            pageResponseDTO.setDtoList(newProducts);
        } catch (Exception e) {
            for (Products p : products) {
                p.setLikeState(0);
            }
            pageResponseDTO.setDtoList(products);
        }
        model.addAttribute("view", view);
        model.addAttribute("pageResponseDTO", pageResponseDTO);}
    else{
            view = 1;
            List<Products> products = mainService.searchListForCate(requestDTO.getBoard());
            try {
                User user = userDetails.getUser();
                products  = mainService.hahaha(products, user.getUid());
            } catch (Exception e) {
                for (Products p : products) {
                    p.setLikeState(0);
                }
            }
            model.addAttribute("state", requestDTO.getBoard());
            model.addAttribute("products", products);
            model.addAttribute("view", view);

        }


        return "/product/list";
    }

    //상품 상세페이지 조회
    @GetMapping("/product/view")
    public String view(Model model, ReviewPageRequestDTO reviewPageRequestDTO , CsFaqPageRequestDTO csFaqPageRequestDTO) {

        int prodno = reviewPageRequestDTO.getProdno();

        log.info("prodno 값 : " + reviewPageRequestDTO.getProdno());

        //배너 조회
        List<BannerDTO> banner3 = adminService.findCateBanner("PRODUCT1");

        model.addAttribute("banner3", banner3);

        //qna 조회
        model.addAttribute("prodQna", productQnaService.productQnas(csFaqPageRequestDTO , prodno));

        //상품 조회
        ProductsDTO productsDTO = marketService.selectProduct(prodno);
        //그거 저장
        marketService.updateProductSearchCount(prodno);


        //subProducts에서 prodno로 조회, color과 size의 리스트를 들고 온다
        List<SubProducts> Options = marketService.findAllByProdNo(prodno);

        //리뷰수를 조회
        Products products =  marketService.findProduct(prodno);

        //리뷰 각 점수를 합계
        Map<Integer,Integer> scoreCountMap= reviewService.sumScore(prodno);

        //각 리뷰 점수를 map으로 생성했음
        model.addAttribute("scoreCountMap",scoreCountMap);

        log.info("리뷰 점수를 출력해보자 : "+scoreCountMap.get(5));

        model.addAttribute("reviewNum",products);

        log.info("Options : " + Options.size());

        log.info("options : " + Options);

        log.info("view - getMapping - productsDTO : " + productsDTO);

        log.info("/product/view : 여기까지 들어오는건가?");

        model.addAttribute("options", Options);

        //리뷰 조회
        ReviewPageResponseDTO reviewPageResponseDTO = reviewService.selectReviews(prodno, reviewPageRequestDTO);

        log.info("페이지 네이션 할 prodview - reviewPageResponseDTO : " + reviewPageResponseDTO);

        //리뷰 별점 - 평균, 비율 구하기
        ReviewRatioDTO reviewRatioDTO = reviewService.selectForRatio(prodno);

        model.addAttribute("product", productsDTO);//제품정보와 이미지정보도 같이 담은 productsDTO
        model.addAttribute("reviewPage", reviewPageResponseDTO);
        model.addAttribute("reviewRatioDTO",reviewRatioDTO);

        return "/product/view";
    }

    //장바구니 넣기
    @PostMapping("/product/view")
    public ResponseEntity<Map<String, String>> view(@RequestBody Map<String, Object> map) {
        log.info(map.get("itemsCounts").toString());
        log.info(map.get("itemsNos").toString());
        List<Integer> nos = (List<Integer>) map.get("itemsNos");
        List<Integer> counts = (List<Integer>) map.get("itemsCounts");
        String uid = map.get("userId").toString();
        return marketService.inserCart(uid, counts, nos);
    }

    ;

    @ResponseBody
    @PostMapping("/product/order")
    public ResponseEntity order(@RequestBody Map<String, Object> map, HttpSession session) {
        List<Integer> nos = (List<Integer>) map.get("itemsNos");
        List<Integer> counts = (List<Integer>) map.get("itemsCounts");
        session.setAttribute("nos", nos);
        session.setAttribute("counts", counts);

        Map<String, String> map1 = new HashMap<>();
        map1.put("result", "success");
        return ResponseEntity.ok().body(map1);
    }

    //바로구매(장바구니 안 거치고)
    @GetMapping("/product/order")//
    public String order(Model model, HttpSession session, Authentication authentication) {
        int status = 0;
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        List<Integer> nos = (List<Integer>) session.getAttribute("nos");
        List<Integer> counts = (List<Integer>) session.getAttribute("counts");

        log.info("nos nonono~ : "+nos);

        List<SubProducts> tempSubproduct = marketService.selectProducts(nos, counts);
        Map<String, List<SubProducts>> sellerProductMap = new HashMap<>();
        for (SubProducts subProduct : tempSubproduct) {
            String sellerName = subProduct.getProducts().getSellerName();
            List<SubProducts> productList = sellerProductMap.getOrDefault(sellerName, new ArrayList<>());
            productList.add(subProduct);
            sellerProductMap.put(sellerName, productList);
        }
        log.info(sellerProductMap.toString() + "here~");

        //쿠폰 리스트도 걍 넘겨주자여기서
        model.addAttribute( "coupons",marketService.searchCoupon(user.getUid()));
        model.addAttribute("subProducts", sellerProductMap);
       // model.addAttribute("counts", counts);
        model.addAttribute("number", counts.size()); // 이거를 넣음 주문건수 때문에
        model.addAttribute("status", status);
        return "/product/order";
    }

    //바로구매(장바구니 거치고) 이것만 좀 제대로 수정함 아 귀찮아 그냥 구매 카트 등은 이거 코드보고 나중에 수정하자..
    @GetMapping("/product/order2")//
    public String order2(Model model, Authentication authentication, @RequestParam(name = "list") List<Integer> list) {
        log.info("list : " + list);
        int status = 1;
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);


        List<Carts> cartsList = marketService.selectCarts(list);
        List<Integer> counts = cartsList.stream().map(item ->   item.getCartProdCount()).toList();
        List<Integer> nos = cartsList.stream().map(item ->   item.getProdNo()).toList();
        model.addAttribute("cartlist", cartsList);

        List<SubProducts> tempSubproduct = marketService.selectProducts(nos , counts);
        Map<String, List<SubProducts>> sellerProductMap = new HashMap<>();
        for (SubProducts subProduct : tempSubproduct) {
            String sellerName = subProduct.getProducts().getSellerName();
            List<SubProducts> productList = sellerProductMap.getOrDefault(sellerName, new ArrayList<>());
            productList.add(subProduct);
            sellerProductMap.put(sellerName, productList);
        }
        log.info(sellerProductMap.toString() + "here~");
        //쿠폰 리스트도 걍 넘겨주자여기서
        model.addAttribute( "coupons",marketService.searchCoupon(user.getUid()));

        model.addAttribute("subProducts", sellerProductMap);
        //  model.addAttribute("counts", counts);
        model.addAttribute("number", counts.size()); // 이거를 넣음 주문건수 때문에
        model.addAttribute("status", status);
        return "/product/order";
    }

    //구매하기 (orderTable  포인트 사용 감소)
    @ResponseBody
    @PostMapping("/product/orderBuy")
    public ResponseEntity orderBuy(@RequestBody OrdersDTO ordersDTO) {
        log.info("이거확인 " + ordersDTO.toString());
        return marketService.insertOrderAndPoint(ordersDTO);

    }

    //구매하기2( 카트제거 , orderItems 넣기)
    @ResponseBody
    @PostMapping("/product/deleteCartForOrder")
    public ResponseEntity deleteCartForOrder(@RequestBody Map<String, List<Integer>> map) {
        List<Integer> list = map.get("lists");
        return marketService.deleteCartForBuy(list);
    }

    //구매하기2-1 (세션제거, orderItems 넣기)
    @ResponseBody
    @GetMapping("/product/insertItems")
    public ResponseEntity insertItems(Model model, HttpSession session,
                                      @RequestParam(name = "orderNo") int orderNo, Authentication authentication) {
        List<Integer> nos = (List<Integer>) session.getAttribute("nos");
        List<Integer> counts = (List<Integer>) session.getAttribute("counts");
        session.removeAttribute("nos");
        session.removeAttribute("counts");

        return marketService.insertItemsForBuy(nos, counts, orderNo);
    }

    @GetMapping("/product/orderSuccess")
    public String successOrder(@RequestParam(name = "orderNo") int orderNo, Model model){


        model.addAttribute("orderItems", marketService.findOrderItems(orderNo));
        model.addAttribute("order", marketService.findOrder(orderNo));
        return "/product/complete";
    }


    //주문 취소시 사용한 포인트 돌려주기 + userTotalPrice 빼기

    //옵션관련된것
    @ResponseBody
    @GetMapping("/product/optionSelect")
    public ResponseEntity optionSelect(@RequestParam(name = "color") String color,
                                       @RequestParam(name = "prodNo") int prodNo) {

        return marketService.selectOption(color, prodNo);
    }

    @GetMapping("/product/cart")
    public String cart(Authentication authentication, Model model) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<Carts> carts = marketService.selectCart(user.getUid());
        List<Integer> subProdnos = carts.stream().map(e -> e.getProdNo()).toList();
        model.addAttribute("subProducts", marketService.selectProductsForCart(subProdnos));
        model.addAttribute("carts", carts);
        return "/product/cart";

    }

    //장바구니 삭제
    @ResponseBody
    @PutMapping("/product/cart/delete")
    public ResponseEntity deleteCart(@RequestBody Map<String, List<Integer>> map) {
        log.info(map.toString());
        return marketService.deleteCart(map.get("list"));

    }

    @GetMapping("/product/search")
    public String search(String cate, String keyword, ProductsPageRequestDTO requestDTO, Model model, HttpSession session) {

        log.info("cate : " + cate);
        marketService.updateKeyword(keyword);

        ProductsPageResponseDTO searchResult = mainService.searchForProduct(requestDTO);

        log.info("지금 내가 작성한 쿼리문이 잘 돌고 있나 체크 search : " + searchResult);

        model.addAttribute("searchResult", searchResult);

        return "/product/search";
    }


    @GetMapping("/product/complete")
    public String complete() {

        return "/product/complete";
    }

    //주문취소/반품
    @GetMapping("/product/checkOrder")
    public ResponseEntity checkOrder(Model model, @RequestParam(name = "itemNo") int itemNo) {
        return marketService.checkOrder(itemNo);
    }


    @GetMapping("/product/orderDelete")
    public ResponseEntity orderDelete(Model model, @RequestParam(name = "itemNo") int itemNo, @RequestParam(name= "excuse") String excuse, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return marketService.orderDelete(itemNo, user.getUid() , excuse);

    }


    //like
    @GetMapping("/product/like")
    public ResponseEntity like(@RequestParam(name = "userId") String uid, @RequestParam(name = "prodNo") int prodNo) {
        return marketService.likeButton(uid, prodNo);
    }


    //like

    @GetMapping("/product/likeSearch")
    public ResponseEntity likeSearch(@RequestParam(name = "userId") String uid, @RequestParam(name = "prodNo") int prodNo) {
        return marketService.search(uid, prodNo);
    }

    //구매확정
    @GetMapping("/product/completeOrder")
    public ResponseEntity completeOrder(@RequestParam(name = "itemNo") int itemNo) {
        return marketService.completeOrder(itemNo);
    }

    //장바구니 수량변경
    @GetMapping("/product/modifyCount")
    public ResponseEntity modifyCount(@RequestParam(name = "count")int count, @RequestParam(name="cartNo") int cartNo ){
        return marketService.modifyCount(count, cartNo);

    }

}