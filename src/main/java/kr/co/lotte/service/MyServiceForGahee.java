package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.OrdersItemRepository;
import kr.co.lotte.repository.OrdersRepository;
import kr.co.lotte.repository.PointsRepository;
import kr.co.lotte.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyServiceForGahee {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private OrdersItemRepository ordersItemRepository;
    @Autowired
    private ProductsRepository productsRepository;

    public List<Points> forPoint(String uid){
        //최근 주문 내역
        //포인트 적립내역
        return pointsRepository.findAllByUserId(uid);
        //상품평은 아람님께 부탁드리자..
        //문의내역은 상도님께
    }

    public PointsPageResponseDTO searPoints(PointsPageRequestDTO requestDTO , String uid){
        Pageable pageable = requestDTO.getPageable("no");
        Page<Points> page = pointsRepository.searchAllPointsForList(requestDTO, pageable, uid);
        List<Points> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return  new PointsPageResponseDTO(requestDTO, dtoList ,total);

    }

    public OrdersPageResponseDTO searchOrders(OrdersPageRequestDTO requestDTO, String uid) throws ParseException {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Orders> page = ordersRepository.searchAllOrders(requestDTO, pageable, uid);
        List<Orders> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new OrdersPageResponseDTO(requestDTO,dtoList, total);
    }

    public List<List<OrderItems>> searchOrderItems(List<Orders> orders) {
        List<List<OrderItems>> lists = new ArrayList<>();
        for (Orders order : orders) {
            List<OrderItems> nLists = ordersItemRepository.findAllByOrderNo(order.getOrderNo());
            lists.add(nLists);
        }
        return lists;
    }

    public List<Products> searchProducts(List<List<OrderItems>> orders) {
        List<Products>  lists = new ArrayList<>();
        for (List<OrderItems> orderItems: orders) {
            Products products = productsRepository.findById(orderItems.get(0).getProdNo()).get();
            lists.add(products);
        }
        return lists;
    }
}
