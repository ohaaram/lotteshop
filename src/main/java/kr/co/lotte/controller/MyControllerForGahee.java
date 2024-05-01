package kr.co.lotte.controller;

import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.dto.OrdersPageResponseDTO;
import kr.co.lotte.dto.PointsPageRequestDTO;
import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.Orders;
import kr.co.lotte.security.MyUserDetails;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MyServiceForGahee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyControllerForGahee {
    private final AdminService adminService;
    private final MyServiceForGahee myServiceForGahee;

    @GetMapping("/my/coupon")
    public String myCoupon() {
        return "/my/coupon";
    }


    @GetMapping("/my/home")
    public String myHome(Model model , Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uid = userDetails.getUser().getUid();
        //포인트 넣어주고
        model.addAttribute("points", myServiceForGahee.forPoint(uid));
        //최근 주문 내역

        List<BannerDTO> banner5 = adminService.findMY1("MY1");
        log.info("banner5: {}", banner5);
        model.addAttribute("banner5", banner5);

        return "/my/home";
    }

    @GetMapping("/my/order")
    public String myOrder(OrdersPageRequestDTO requestDTO, Authentication authentication, Model model) throws ParseException {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uid = userDetails.getUser().getUid();
        OrdersPageResponseDTO pageResponseDTO = myServiceForGahee.searchOrders(requestDTO, uid);
        List<Orders> orders = pageResponseDTO.getDtoList();
        List<List<OrderItems>> orderItems = myServiceForGahee.searchOrderItems(orders);
        //subitems (subProdNo가 나올 것이고...)
        model.addAttribute("orderItems", myServiceForGahee.searchOrderItems(orders));

        //prod image
        model.addAttribute("products",myServiceForGahee.searchProducts(orderItems));

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "/my/order";
    }

    @GetMapping("/my/point")
    public String myPoint(PointsPageRequestDTO requestDTO, Authentication authentication , Model model) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uid = userDetails.getUser().getUid();
        model.addAttribute("pageResponseDTO", myServiceForGahee.searPoints(requestDTO,uid));


        return "/my/point";
    }

    @GetMapping("/my/qna")
    public String myQna() {
        return "/my/qna";
    }



}
