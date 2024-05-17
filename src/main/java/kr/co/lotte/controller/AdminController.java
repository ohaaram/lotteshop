package kr.co.lotte.controller;


import com.querydsl.core.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.BannerRepository;
import kr.co.lotte.security.MyManagerDetails;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.BlogService;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.ProductQnaService;
import kr.co.lotte.service.cs.CsNoticeService;
import kr.co.lotte.service.cs.CsQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.security.AlgorithmConstraints;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private AdminService adminService;
    private final CsNoticeService csNoticeService;
    private final CsQnaService csQnaService;
    private final ProductQnaService productQnaService;
    private final BlogService blogService;
    @Autowired
    private MemberService memberService;

    @GetMapping(value = {"/admin/index", "/admin"})
    public String adminIndex(Authentication authentication, Model model, HttpSession session) {



        try {
            //여기는 관리자
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            Map<String, Integer> map = adminService.Formain(session);
            int count = map.get("count");
            int total = map.get("total");
            int user = map.get("user");

            int ready = map.get("ready");
            int delivery = map.get("delivery");
            int allDelete = map.get("allDelete");
            int delete = map.get("delete");

            model.addAttribute("count", count);
            model.addAttribute("total", total);
            model.addAttribute("user", user);
            int visitor = map.get("visitors");

            model.addAttribute("ready", ready);
            model.addAttribute("delivery", delivery);
            model.addAttribute("delete", delete);
            model.addAttribute("allDelete", allDelete);
            model.addAttribute("visitor", visitor);

            model.addAttribute("csNotice", csNoticeService.noticeList());
            model.addAttribute("csQna", csQnaService.qnaList());


            return "/admin/index";

        } catch (Exception e) {
            //여기는 매니저
            MyManagerDetails myManagerDetails = (MyManagerDetails) authentication.getPrincipal();
            Seller seller = myManagerDetails.getUser();
            String uid = seller.getSellerUid();

            Map<String, Integer> map = adminService.forManager(uid);
            int count = map.get("count");
            int total = map.get("total");
            int user = map.get("user");
            int totalArticle = map.get("totalArticle");

            model.addAttribute("totalArticle", totalArticle);
            model.addAttribute("count", count);
            model.addAttribute("total", total);
            model.addAttribute("user", user);

            int ready = map.get("ready");
            int delivery = map.get("delivery");
            int delete = map.get("delete");
            int allDelete = map.get("allDelete");
            int visitor = map.get("visitors");

            model.addAttribute("ready", ready);
            model.addAttribute("delivery", delivery);
            model.addAttribute("delete", delete);
            model.addAttribute("allDelete", allDelete);
            model.addAttribute("visitor", visitor);


            model.addAttribute("prodQna", productQnaService.prodSellerQna(uid));


            return "/admin/index2";
        }

    }
    ///판매자 FAQ 수정 (판매자 상품 문의만 뜨도록(where.qFQA.equl(seller.uid(selluid)) + 답변)
    //상품상세보기 리뷰 (해당 상품만 뜨도록 여기는 where조건을 equal.prodNo(.../))
    //config
    @GetMapping("/admin/config/banner")
    public String banner(Model model) {

        List<BannerDTO> banner1 = adminService.findCateBanner("MAIN1");
        List<BannerDTO> banner2 = adminService.findCateBanner("MAIN2");
        List<BannerDTO> banner3 = adminService.findCateBanner("PRODUCT1");
        List<BannerDTO> banner4 = adminService.findCateBanner("MY1");

        log.info("AdminController - banner : " + banner1.toString());

        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);
        model.addAttribute("banner3", banner3);
        model.addAttribute("banner4", banner4);

        return "/admin/config/banner";
    }

    //info
    @GetMapping("/admin/config/info")
    public String info(Model model, @RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            code = "0";
        }
        model.addAttribute("code", code);
        model.addAttribute("terms", adminService.findTerms(1));
        return "/admin/config/info";
    }

    //info modify
    @PostMapping("/admin/info/modify")
    public String modifyInfo(TermsDTO termsDTO) {
        adminService.modifyTerms(termsDTO);
        return "redirect:/admin/config/info?code=100";
    }


    @PostMapping("/admin/product/subOptionModify")
    public ResponseEntity modifyOption(@RequestBody Map<String, List<SubProductsDTO>> map){
        List<SubProductsDTO> list = map.get("list");
        return adminService.modifyOptions(list);
    }


    //product
    @GetMapping("/admin/product/list")
    public String list(Model model, ProductsPageRequestDTO pageRequestDTO, Authentication authentication) {

        String uid = null;
        Boolean isSeller = false;

        if (pageRequestDTO.getCate() != null) {
            log.info(pageRequestDTO.getCate());
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) principal;
            uid = userDetails.getUsername();
        } else if (principal instanceof MyManagerDetails) {
            MyManagerDetails myManagerDetails = (MyManagerDetails) principal;
            Seller seller = myManagerDetails.getUser();
            uid = seller.getSellerUid();
        }

        if (uid != null) {
            isSeller = adminService.findBySeller(uid);
        }

        if (pageRequestDTO.getCate() == null) {
            pageRequestDTO.setCate(""); // 여기서 cate 필드를 빈 문자열로 초기화
        }

        ProductsPageResponseDTO pageResponseDTO;

        if (isSeller) {

            log.info("여기는 매니저일 떄 들어오는 곳");

            pageResponseDTO = adminService.searchProductsForManager(pageRequestDTO, uid);
        } else {

            log.info("여기는 관리자일 때 들어오는 곳");

            pageResponseDTO = adminService.searchProducts(pageRequestDTO);

        }

        model.addAttribute("page", pageResponseDTO);
        model.addAttribute("isSeller", isSeller);

        return "/admin/product/list";
    }

    @GetMapping("/admin/product/register")
    public String register(Model model, @RequestParam(name = "code", required = false) String code) {
        List<Categories> list = adminService.searchCategories();
        List<String> cateNameLists = list.stream().map(categories -> categories.getCateName()).toList();
        Set<String> cateNames = cateNameLists.stream().collect(Collectors.toSet());
        log.info(list.toString());
        model.addAttribute("cates", list);
        model.addAttribute("cateNames", cateNames);
        if (code == null) {
            model.addAttribute("code", 0);
        } else {
            model.addAttribute("code", code);
        }
        return "/admin/product/register";
    }


    @ResponseBody
    @PostMapping("/admin/product/selectSecondCate")
    public ResponseEntity selectSecondCate(@RequestBody Map<String, String> map) {
        String name = map.get("cate");
        return adminService.searchCategoriesSecondNames(name);
    }

    @ResponseBody
    @PostMapping("/admin/product/selectThridCate")
    public ResponseEntity selectThridCate(@RequestBody Map<String, String> map) {
        String name = map.get("cate");
        return adminService.searchCategoriesThridNames(name);
    }

    @PostMapping("/admin/product/register")
    public String register(ProductsDTO productsDTO) {
        log.info(productsDTO.toString());
        adminService.productRegister(productsDTO);
        return "redirect:/admin/product/register?code=100";
    }

    @PostMapping("/admin/product/modify")
    public String modify( ProductsDTO productsDTO){
        log.info(productsDTO.toString());
        adminService.productModify(productsDTO);
        return  "redirect:/admin/product/modify?code=200&prodNo="+productsDTO.getProdNo();
    }



    @ResponseBody
    @PostMapping("/admin/product/subOption")
    public ResponseEntity registersubOption(@RequestBody List<SubProductsDTO> subProductsDTOS) {
        log.info(subProductsDTOS.toString());
        return adminService.insertSubOptions(subProductsDTOS);
    }

    ;

    //상품수정
    @GetMapping("/admin/product/modify")
    public String modify(Model model, @RequestParam int prodNo) {
        log.info(prodNo + "prodNo");
        model.addAttribute("products", adminService.findOnlyOneProduct(prodNo));
        model.addAttribute("subProducts", adminService.subProductsFind(prodNo));
        return "/admin/product/modify";
    }

    //아 상품수정 나중에 할래 귀찮다


    //상품 삭제
    @ResponseBody
    @PutMapping("/admin/product/delete")
    public ResponseEntity delete(@RequestBody Map<String, List<Integer>> map) {
        List<Integer> lists = map.get("list");
        log.info(lists.toString());
        adminService.deleteProducts(lists);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.ok().body(map);
    }

    //하나만 삭제
    @PostMapping("/admin/product/deleteOne")
    public void deleteOne(@RequestBody Map<String, Integer> map) {
        int subNo = map.get("number");
        log.info(subNo + "subNo");
        adminService.deleteProduct(subNo);
    }


    //배너에 등록에 대한 컨트롤러
    @ResponseBody
    @PostMapping("/banner/register")
    public ResponseEntity<String> register(BannerDTO bannerDTO) {

        log.info("정상적으로 여기에 들어와지니?");

        adminService.bannerRegister(bannerDTO);//이미지 등록완료!

        log.info(bannerDTO.toString());//제대로 들어와짐

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/lotteshop/admin/config/banner")).build();

    }

    //선택삭제를 위한 컨트롤러
    @PostMapping("/banner/delete")
    public ResponseEntity<?> SelectDelete(@RequestBody Map<String, String> requestData) {

        log.info("삭제하려고 controller로 왔습니다.");

        String bannerNo = requestData.get("bannerNo");

        log.info("bannerNo:" + bannerNo);

        adminService.bannerDelete(bannerNo);

        log.info("BannerNo로 삭제를 성공했느냐? ");

        Map<String, String> result = new HashMap<>();
        result.put("data", "1");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/banner/active")
    public ResponseEntity<?> bannerActive(@RequestParam("bannerNo") String bannerNo, RedirectAttributes redirectAttributes) {

        log.info("bannerNo : " + bannerNo);//배너번호 잘 넘어옴

        Map<String, Object> result = new HashMap<>();

        try {

            BannerDTO banner = adminService.findById(bannerNo);//배너번호를 이용해서 설정하기 내용은 읽어오기!

            return ResponseEntity.ok(banner);

        } catch (AdminService.SomeException e) {
            // SomeException 예외 발생 시 처리

            log.info(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/banner/inactive")
    public ResponseEntity<?> bannerInactive(@RequestParam("bannerNo") String bannerNo) {

        Map<String, Object> result = new HashMap<>();

        adminService.findByIdForDelete(bannerNo);//status 0으로 바꾸기

        result.put("success", 1);
        return ResponseEntity.ok().body(result);
    }


    //주문현황
    @GetMapping("/admin/orderList")
    public String orderList(Model model, Authentication authentication, OrdersPageRequestDTO requestDTO) throws ParseException {
        OrdersPageResponseDTO pageResponseDTO = null;
        List<SubProducts> products = new ArrayList<>();
        //모두 조회 해주자..
        try {
            //여기는 관리자
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            pageResponseDTO = adminService.searchOrdersForAdmin(requestDTO);
            log.info(pageResponseDTO.getDtoList2().toString());
            products = adminService.searchProductsForOrder(pageResponseDTO.getDtoList2());

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("??");
            MyManagerDetails myManagerDetails = (MyManagerDetails) authentication.getPrincipal();
            Seller seller = myManagerDetails.getUser();
            pageResponseDTO = adminService.searchOrdersForManager(requestDTO, seller.getSellerUid());
            products = adminService.searchProductsForOrder(pageResponseDTO.getDtoList2());
        }
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("products", products);
        return "/admin/order/order";
    }

    //주문 상세 현황
    @GetMapping("/admin/orderDetail")
    public String orderDetail(@RequestParam(name = "orderNo") int orderNo, Model model) {
        model.addAttribute("order", adminService.forOrderDetail(orderNo));
        return "/admin/order/orderView";
    }


    //판매자 현황 띄우기
    @GetMapping("/admin/seller/seller_status")
    public String seller_status(Model model, CsFaqPageRequestDTO pageRequestDTO) {

        StatusPageResponseDTO pageResponseDTO = null;

        pageResponseDTO = adminService.seller_status(pageRequestDTO);

        List<Seller> sellerList =  adminService.waitingSellers();

        model.addAttribute("sellerList",sellerList);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        log.info("판매자 리스트(temp) : "+sellerList);

        log.info("adminController - seller_status : " + pageResponseDTO.toString());


        return "/admin/seller/seller_status";
    }

    //판매자 상태 바꾸기(TEMP->MANAGER)
    @GetMapping("/admin/sellerTrans")
    public ResponseEntity<?> trans(String sellerUid){

        Seller seller = adminService.changeRole(sellerUid);

        Map<String, String> result = new HashMap<>();

        String email = seller.getSellerEmail();

        log.info("AdminController - trans - email : " + email);

        if(seller!=null){

            result.put("result", "1");

            //이메일 보내기
           memberService.sendEmailForSeller(email);

            return ResponseEntity.ok().body(result);

        }else{

            result.put("result", "0");
            return ResponseEntity.ok().body(result);

        }
    }

    //판매자 모달창에 띄울 정보들
    @GetMapping("/admin/seller/{uid}")
    public ResponseEntity<?> modal(@PathVariable("uid") String uid) {


        SellerDTO sellerDTO = adminService.findSellerInfo(uid);//판매자 정보

        log.info("adminController - modal - sellerDTO : " + sellerDTO);

        // Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", sellerDTO);

        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("/admin/checkOrder")
    public ResponseEntity checkOrder(@RequestParam(name = "orderNo") int orderNo, Model model) {
        log.info("들어옴!");
        return adminService.changeOrderState(orderNo);

    }

    //주문 바꾸기들
    @PutMapping("/admin/checkOrders")
    public ResponseEntity checkOrders(@RequestBody Map<String, List<Integer>> map) {
        log.info(map.get("list").toString());
        return adminService.changeOrderStates(map.get("list"));

    }

    //매출현황 가보자고
    @GetMapping("/admin/sale")
    public String sale(Model model, Authentication authentication, @RequestParam(name = "state") String state) {
        try {
            //여기는 관리자 (매출은 어떻게 집계하지? order에 totalPrice가 있으니까 그걸로 해볼까?)
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            Map<String, List<Integer>> map = adminService.saleForAdmin(state);
            List<Integer> price = map.get("price");
            List<Integer> order = map.get("order");
            List<String> days = adminService.forDays(state);

            model.addAttribute("days", days);
            model.addAttribute("price", price);
            model.addAttribute("order", order);

        } catch (Exception e) {
            //여기는 판매자
            MyManagerDetails myManagerDetails = (MyManagerDetails) authentication.getPrincipal();
            Seller seller = myManagerDetails.getUser();
            Map<String, List<Integer>> map = adminService.saleForManager(state, seller.getSellerUid());
            List<Integer> price = map.get("price");
            List<Integer> order = map.get("order");
            List<String> days = adminService.forDays(state);

            model.addAttribute("days", days);
            model.addAttribute("price", price);
            model.addAttribute("order", order);

        }
        /*
        adminService.saleForAdmin(state);
        List<Integer> listA = new ArrayList<>();
        listA.add(1);
        listA.add(2);
        listA.add(3);
        List<Integer> listB = new ArrayList<>();
        listB.add(4);
        listB.add(5);
        listB.add(6);
        List<String> listC = new ArrayList<>();
        listC.add("1월");
        listC.add("2월");
        listC.add("3월");
        model.addAttribute("listA", listA);
        model.addAttribute("listB", listB);
        model.addAttribute("listC", listC);

         */
        return "/admin/order/sale";

    }

    //블로그 모든 글 띄우기
    @GetMapping("/admin/blogList")
    public String blogList(Model model, BlogPageRequestDTO pageRequestDTO) {

        BlogPageResponseDTO findAllList = blogService.findAllList(pageRequestDTO);

        model.addAttribute("blog",findAllList);

        return "/admin/blog/list";
    }


    //블로그 글쓰기 페이지 이동
    @GetMapping("/admin/blogWrite")
    public String blogWrite() {

        return "/admin/blog/write";
    }

    //블로그 글 정보 전송
    @PostMapping("/admin/blogWrite")
    public String blogRegister(BlogDTO blogDTO, HttpServletResponse response) throws IOException {

        log.info("블로그 글 정보 전송하는 컨트롤러");

        log.info("전송되어온 데이터 출력 : "+blogDTO);

        blogService.bRegister(blogDTO);

        return "redirect:/admin/blogList";
    }

    //블로그 글 상세보기
    @GetMapping("/admin/blogView/{bno}")
    public String blogView(@PathVariable("bno") int bno, Model model) {

        log.info("blogView Get - controller입니다.");

        Blog blog = blogService.BlogFindById(bno);

        model.addAttribute("blog", blog);

        log.info("bno 값 : "+bno);

        return "/admin/blog/view";
    }


    //글 삭제
    @GetMapping("/admin/blogDel/{bno}")
    public ResponseEntity<?> blogDelete(@PathVariable("bno")int bno){

        log.info("여기는 AdminController - blogDelte");

        blogService.blogDel(bno);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", true);

        return ResponseEntity.ok().body(resultMap);

    }


    //블로그 글 가져오기(수정을 위해서)
    @GetMapping("/admin/blogModify")
    public String modify(@RequestParam("bno")int bno,Model model){

        log.info("AdminController - modify - bno : "+bno);

        Blog blog = blogService.findBlog(bno);

        model.addAttribute("blog",blog);

        return "/admin/blog/modify";
    }

    //블로그 글 수정한거 저장하기
    @PostMapping("/admin/blogModify")
    public String modiBlog(BlogDTO blogDTO){

        log.info("여기는 adminController - modiBlog - BlogDTO : "+blogDTO.toString());

        blogService.modifyBlog(blogDTO);

        log.info("수정 완료!");

        return "redirect:/admin/blogList";//여기서는 전체 글로 이동
    }
}
