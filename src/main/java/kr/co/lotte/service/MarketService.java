package kr.co.lotte.service;


import com.querydsl.core.Tuple;
import groovy.transform.AutoImplement;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.expr.NewArray;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MarketService {

    private final ProductsRepository marketRepository;
    private final ModelMapper modelMapper;
    private final SubProductsRepository subProductsRepository;
    private final ProductsRepository productRepository;
    private final CartsRepository cartsRepository;

    // 장보기 글보기 페이지 - 장보기 게시글 출력
    public ProductsDTO selectProduct(int prodno){

        return modelMapper.map( productRepository.findById(prodno).get() , ProductsDTO.class);

    }


    public List<SubProducts> findAllByProdNo(int prodno){

        //prodno의 모든 데이터를 출력
        List<SubProducts> Options = subProductsRepository.findAllByProdNo(prodno);

        return Options;
    }

    public ResponseEntity selectOption(String value, int prodNo){
        List<SubProducts> Options2 = subProductsRepository.findAllByProdNoAndColor(prodNo, value);
        Map<String, List<SubProducts>> map = new HashMap<>();
        map.put("sub", Options2);
        log.info(Options2.toString()+"?!!!");
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity inserCart(String userId, List<Integer> counts , List<Integer> subNos){
        int i=0;
        for(int count : counts){
            CartsDTO cartsDTO = new CartsDTO();
            cartsDTO.setCartProdCount(count);
            cartsDTO.setProdNo(subNos.get(i));
            cartsDTO.setUserId(userId);
            cartsRepository.save(modelMapper.map(cartsDTO , Carts.class));
            i++;
        }
        Map<String , String> map = new HashMap<>();
        map.put("result" , "success");
        return ResponseEntity.ok().body(map);
    }

    //장바구니 삭제
    public ResponseEntity deleteCart(List<Integer> lists){
        for(Integer count : lists){
            cartsRepository.deleteById(count);
        }
        Map<String , String> map = new HashMap<>();
        map.put("result" , "success");
        return ResponseEntity.ok().body(map);
    }


    //카트조회
    public List<Carts> selectCart(String userId){
        return cartsRepository.findAllByUserId(userId);
    }
    //카트상품조회
    public List<SubProducts> selectProducts(List<Integer> subProductsNo){
        List<SubProducts> lists = new ArrayList<>();
        for(int a : subProductsNo){
            Tuple sub = productRepository.serachOnlyOne(a);
            SubProducts subProducts = sub.get(1, SubProducts.class);
            subProducts.setProducts(sub.get(0, Products.class));
            lists.add(subProducts);
        }
        return lists;
    }
    //카트조회(주문을 위한)
    public List<Carts> selectCarts(List<Integer> cartNos){
        List<Carts> lists = new ArrayList<>();
        for(int a : cartNos){
            lists.add(cartsRepository.findById(a).get());
        }
        return lists;
    }

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrdersItemRepository ordersItemRepository;
    @Autowired
    private PointsRepository pointsRepository;

    //order table 넣고 포인트 감소
    public ResponseEntity insertOrderAndPoint(OrdersDTO ordersDTO){
        ordersDTO.setOrderState("주문 대기");
       Orders orders= ordersRepository.save(modelMapper.map(ordersDTO , Orders.class));
        if(ordersDTO.getPoint()>0){
            User user = memberRepository.findById(ordersDTO.getUserId()).get();
            user.setTotalPoint(user.getTotalPoint()-ordersDTO.getPoint());
            memberRepository.save(user);

            Points points = new Points();
            points.setUserId(user.getUid());
            points.setPoint(-ordersDTO.getPoint());
            points.setPointDesc("상품 구매 사용");
            points.setState("차감");

            points.setOrderNo(orders.getOrderNo());
            pointsRepository.save(points);

            Points points1 = new Points();
            points1.setUserId(user.getUid());
        }
        Map<String , Integer> map = new HashMap<>();
        map.put("orderNo" , orders.getOrderNo());
        return ResponseEntity.ok().body(map);
    }

    //orderItems 넣고 카트 제거 포인트 추가, 재고 감소
    public ResponseEntity deleteCartForBuy(List<Integer> lists){
        int orderNo = lists.get(0);
        lists.remove(0);
        int point = 0;
        String userId = cartsRepository.findById(lists.get(0)).get().getUserId();

        for(int carNo : lists){
            Carts carts = cartsRepository.findById(carNo).get();
            OrderItemsDTO orderItemsDTO  = new OrderItemsDTO();
            orderItemsDTO.setOrderNo(orderNo);
            orderItemsDTO.setProdNo(carts.getProdNo());
            orderItemsDTO.setItemCount(carts.getCartProdCount());
            orderItemsDTO.setOrderState("주문 대기");

            SubProducts subProducts = subProductsRepository.findById(orderItemsDTO.getProdNo()).get();
            Products products = productRepository.findById(subProducts.getProdNo()).get();

            orderItemsDTO.setItemPrice(subProducts.getProdPrice());
            orderItemsDTO.setItemDiscount(products.getProdDiscount());
            //포인트 추가
            point += (int) (subProducts.getProdPrice() * orderItemsDTO.getItemCount() * 0.01);

            //재고 감소
            subProducts.setProdStock(subProducts.getProdStock() - orderItemsDTO.getItemCount());
            subProductsRepository.save(subProducts);

            //판매량 증가
            products.setProdSold(products.getProdSold() + orderItemsDTO.getItemCount());
            productRepository.save(products);
            
            
            //orderitems 추가, 카트 제거
            ordersItemRepository.save(modelMapper.map(orderItemsDTO , OrderItems.class));
            cartsRepository.deleteById(carNo);
        }
        //포인트 적립
        Points points = new Points();
        points.setUserId(userId);
        points.setPoint(+point);
        points.setPointDesc("상품 구매 적립");
        points.setState("적립");
        points.setOrderNo(orderNo);
        // 현재 날짜 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        // 1년을 더한 날짜 계산
        LocalDateTime oneYearLater = currentDate.plusYears(1);
        points.setEndDateTime(oneYearLater);


        pointsRepository.save(points);

        //토탈포인트 적립
        User user = memberRepository.findById(userId).get();
        user.setTotalPoint(user.getTotalPoint() + point);
        memberRepository.save(user);

        Map<String , String> map = new HashMap<>();
        map.put("data" , "success");
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity insertItemsForBuy(List<Integer> nos, List<Integer> counts , int orderNo){
        int i=0;

        int point = 0;
        String userId = ordersRepository.findById(orderNo).get().getUserId();

        for(int no : nos){
            OrderItemsDTO orderItemsDTO  = new OrderItemsDTO();
            orderItemsDTO.setOrderNo(orderNo);
            orderItemsDTO.setProdNo(no);
            orderItemsDTO.setItemCount(counts.get(i));
            orderItemsDTO.setOrderState("주문 대기");

            SubProducts subProducts = subProductsRepository.findById(orderItemsDTO.getProdNo()).get();
            Products products = productRepository.findById(subProducts.getProdNo()).get();

            orderItemsDTO.setItemPrice(subProducts.getProdPrice());
            orderItemsDTO.setItemDiscount(products.getProdDiscount());
            //포인트 추가
            point += (int) (subProducts.getProdPrice() * orderItemsDTO.getItemCount() * 0.01);

            //재고 감소
            subProducts.setProdStock(subProducts.getProdStock() - orderItemsDTO.getItemCount());
            subProductsRepository.save(subProducts);

            //판매량 증가
            products.setProdSold(products.getProdSold() + orderItemsDTO.getItemCount());
            productRepository.save(products);

            ordersItemRepository.save(modelMapper.map(orderItemsDTO , OrderItems.class));
            i++;
        }
        //포인트 적립
        Points points = new Points();
        points.setUserId(userId);
        points.setPoint(+point);
        points.setPointDesc("상품 구매 적립");
        points.setState("적립");
        points.setOrderNo(orderNo);
        // 현재 날짜 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        // 1년을 더한 날짜 계산
        LocalDateTime oneYearLater = currentDate.plusYears(1);
        points.setEndDateTime(oneYearLater);

        pointsRepository.save(points);

        //토탈포인트 적립
        User user = memberRepository.findById(userId).get();
        user.setTotalPoint(user.getTotalPoint() + point);
        memberRepository.save(user);


        Map<String , String> map = new HashMap<>();
        map.put("data" , "success");
        return ResponseEntity.ok().body(map);
    }

    //주문취소/반품
    //일단 확인
    public ResponseEntity checkOrder(int itemNo){
        Map<String , String> map = new HashMap<>();

        OrderItems orderItems = ordersItemRepository.findById(itemNo).get();
        Orders order = ordersRepository.findById(orderItems.getOrderNo()).get();
        if(order.getPoint() >0){
            map.put("data","1");
        }else{
            map.put("data","0");
        }
        return ResponseEntity.ok().body(map);
    }

    //주문 취소반품 본격시작
    public ResponseEntity orderDelete(int itemNo , String uid){
        Map<String , Integer> map = new HashMap<>();
        User user = memberRepository.findById(uid).get();
        OrderItems orderItems = ordersItemRepository.findById(itemNo).get();
        Orders order = ordersRepository.findById(orderItems.getOrderNo()).get();
        int state =0;
        if(orderItems.getOrderState().equals("주문 대기")){
            if(order.getPoint() >0){
                //포인트를 사용한 경우 전체 취소
                List<OrderItems> lists = ordersItemRepository.findAllByOrderNo(order.getOrderNo());

                for(OrderItems orderItem : lists){
                    if(orderItem.getOrderState().equals("주문 대기")){
                        SubProducts subProducts = subProductsRepository.findById(orderItem.getProdNo()).get();
                        subProducts.setProdStock(subProducts.getProdStock() + orderItem.getItemCount());
                        subProductsRepository.save(subProducts);
                        orderItem.setOrderState("주문 취소");   
                    }else{
                        SubProducts subProducts = subProductsRepository.findById(orderItem.getProdNo()).get();
                        subProducts.setProdStock(subProducts.getProdStock() + orderItem.getItemCount());
                        subProductsRepository.save(subProducts);
                        state=1;
                        orderItem.setOrderState("환불");
                    }
                    ordersItemRepository.save(orderItem);
                }
                //포인트를 돌려주자
                Points points = new Points();
                points.setPoint(order.getPoint());
                points.setPointDesc("주문 취소 환불");
                points.setState("취소환불");
                points.setOrderNo(order.getOrderNo());
                points.setUserId(user.getUid());

                int pointCurrent = order.getPoint();

                //적립된 포인트는 빼자
                Points points2 = new Points();
                Points oldPoint = pointsRepository.findByOrderNoAndState(order.getOrderNo(),"적립");
                int old = oldPoint.getPoint();

                points2.setPoint(-old);
                points2.setPointDesc("주문 취소 환불");
                points2.setState("취소환불");
                points2.setOrderNo(order.getOrderNo());
                points2.setUserId(user.getUid());

                pointCurrent -= old;

                user.setTotalPoint(user.getTotalPoint() + pointCurrent);

                memberRepository.save(user);
                pointsRepository.save(points);
                pointsRepository.save(points2);
            }else{
                SubProducts subProducts = subProductsRepository.findById(orderItems.getProdNo()).get();
                subProducts.setProdStock(subProducts.getProdStock() + orderItems.getItemCount());
                subProductsRepository.save(subProducts);
                orderItems.setOrderState("주문 취소");
                ordersItemRepository.save(orderItems);

                //적립된 포인트는 빼자
                Points points2 = new Points();

                int minus = (int)(orderItems.getItemPrice() * orderItems.getItemCount() * 0.01);

                points2.setPoint(- minus);
                points2.setPointDesc("주문 취소 환불");
                points2.setOrderNo(order.getOrderNo());
                user.setTotalPoint(user.getTotalPoint() - minus);
                memberRepository.save(user);

            }

        }else{
            state=1;

            if(order.getPoint() >0){
                //포인트를 사용한 경우 전체 취소
                List<OrderItems> lists = ordersItemRepository.findAllByOrderNo(order.getOrderNo());

                for(OrderItems orderItem : lists){
                    if(orderItem.getOrderState().equals("주문 대기")){
                        SubProducts subProducts = subProductsRepository.findById(orderItem.getProdNo()).get();
                        subProducts.setProdStock(subProducts.getProdStock() + orderItem.getItemCount());
                        subProductsRepository.save(subProducts);
                        orderItem.setOrderState("주문 취소");
                    }else{
                        SubProducts subProducts = subProductsRepository.findById(orderItem.getProdNo()).get();
                        subProducts.setProdStock(subProducts.getProdStock() + orderItem.getItemCount());
                        subProductsRepository.save(subProducts);
                        orderItem.setOrderState("환불");
                    }
                    ordersItemRepository.save(orderItem);
                }
                //포인트를 돌려주자
                Points points = new Points();
                points.setPoint(order.getPoint());
                points.setPointDesc("주문 취소 환불");
                points.setOrderNo(order.getOrderNo());
                points.setUserId(user.getUid());

                int pointCurrent = order.getPoint();

                //적립된 포인트는 빼자
                Points points2 = new Points();
                Points oldPoint = pointsRepository.findByOrderNoAndState(order.getOrderNo(),"적립");
                int old = oldPoint.getPoint();

                points2.setPoint(-old);
                points2.setPointDesc("주문 취소 환불");
                points2.setState("취소환불");
                points2.setOrderNo(order.getOrderNo());
                points2.setUserId(user.getUid());

                pointCurrent -= old;

                user.setTotalPoint(user.getTotalPoint() + pointCurrent);

                memberRepository.save(user);
                pointsRepository.save(points);
                pointsRepository.save(points2);
            }else{
                SubProducts subProducts = subProductsRepository.findById(orderItems.getProdNo()).get();
                subProducts.setProdStock(subProducts.getProdStock() + orderItems.getItemCount());
                subProductsRepository.save(subProducts);
                orderItems.setOrderState("환불");
                ordersItemRepository.save(orderItems);

                //적립된 포인트는 빼자
                Points points2 = new Points();

                int minus = (int)(orderItems.getItemPrice() * orderItems.getItemCount() * 0.01);

                points2.setPoint(- minus);
                points2.setPointDesc("주문 취소 환불");
                points2.setOrderNo(order.getOrderNo());
                user.setTotalPoint(user.getTotalPoint() - minus);
                memberRepository.save(user);

            }
        
        }
        map.put("data", state);
        return ResponseEntity.ok().body(map);
    }



}
