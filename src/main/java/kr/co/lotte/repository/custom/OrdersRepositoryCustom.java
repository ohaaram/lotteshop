package kr.co.lotte.repository.custom;


import com.querydsl.core.Tuple;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface OrdersRepositoryCustom {
    public Page<Orders> searchAllOrders(OrdersPageRequestDTO pageRequestDTO, Pageable pageable , String uid) throws ParseException;
    public Page<OrderItems> searchAllOrdersForManager(OrdersPageRequestDTO pageRequestDTO, Pageable pageable , String uid) throws ParseException;
    public Page<OrderItems> searchAllOrdersForAdmin(OrdersPageRequestDTO pageRequestDTO, Pageable pageable ) throws ParseException;


    //그래프
    public List<Integer> searchOrdersWeekForAdmin();
    public List<Integer> searchOrdersMonthForAdmin();
    public List<Integer> searchOrdersYearForAdmin();
    
    //매출액
    public List<Integer> searchPriceWeekForAdmin();
    public List<Integer> searchPriceMonthForAdmin();
    public List<Integer> searchPriceYearForAdmin();

    //아 귀찮아.. 매니저용
    //그래프
    public List<Integer> searchOrdersWeekForManager(String uid);
    public List<Integer> searchOrdersMonthForManager(String uid);
    public List<Integer> searchOrdersYearForManager(String uid);

    //매출액
    public List<Integer> searchPriceWeekForManager(String uid);
    public List<Integer> searchPriceMonthForManager(String uid);
    public List<Integer> searchPriceYearForManager(String uid);
}
