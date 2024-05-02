package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MyServiceForGahee {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private OrdersItemRepository ordersItemRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private SubProductsRepository subProductsRepository;
    @Autowired
    private LikeRepository likeRepository;

    public List<Points> forPoint(String uid) {
        //최근 주문 내역
        //포인트 적립내역
        return pointsRepository.findAllByUserId(uid);
        //상품평은 아람님께 부탁드리자..
        //문의내역은 상도님께
    }

    public PointsPageResponseDTO searPoints(PointsPageRequestDTO requestDTO, String uid) {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Points> page = pointsRepository.searchAllPointsForList(requestDTO, pageable, uid);
        List<Points> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new PointsPageResponseDTO(requestDTO, dtoList, total);

    }

    public OrdersPageResponseDTO searchOrders(OrdersPageRequestDTO requestDTO, String uid) throws ParseException {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Orders> page = ordersRepository.searchAllOrders(requestDTO, pageable, uid);
        List<Orders> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new OrdersPageResponseDTO(requestDTO, dtoList, total);
    }

    public List<List<OrderItems>> searchOrderItems(List<Orders> orders) {
        List<List<OrderItems>> lists = new ArrayList<>();
        for (Orders order : orders) {
            List<OrderItems> nLists = ordersItemRepository.findAllByOrderNo(order.getOrderNo());
            lists.add(nLists);
        }
        return lists;
    }

    public List<List<Products>> searchProducts(List<List<OrderItems>> orders) {
        List<List<Products>> lists = new ArrayList<>();
        for (List<OrderItems> orderItems : orders) {
            List<Products> nLists = new ArrayList<>();
            for (OrderItems items : orderItems) {
                SubProducts subProducts = subProductsRepository.findById(items.getProdNo()).get();
                Products products = productsRepository.findById(subProducts.getProdNo()).get();
                nLists.add(products);
            }
            lists.add(nLists);
        }
        return lists;
    }

    public ProductsPageResponseDTO searchLikes(ProductsPageRequestDTO requestDTO, String uid) {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Like> page = productsRepository.searchAllLike(requestDTO, pageable, uid);
        List<Like> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new  ProductsPageResponseDTO(dtoList, requestDTO, total);
    }

    public List<Products> searchProductsLike(List<Like> likes) {
        List<Products> productList = new ArrayList<>();
        for (Like like : likes) {
            Products products1 = productsRepository.findById(like.getProdNo()).get();
            productList.add(products1);
        }
        return productList;
    }

    public ResponseEntity deleteLike(String uid, int prodNo){
        Map<String, String> map = new HashMap<>();

        likeRepository.deleteByUserIdAndProdNo(uid, prodNo);
        map.put("data","1");
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity deleteLikes(String uid, List<Integer> prodNos){
        Map<String, String> map = new HashMap<>();
        for(Integer prodNo : prodNos){
            likeRepository.deleteByUserIdAndProdNo(uid, prodNo);
        }
        map.put("data","1");
        return ResponseEntity.ok().body(map);
    }

}
