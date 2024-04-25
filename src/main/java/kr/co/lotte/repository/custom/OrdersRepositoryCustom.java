package kr.co.lotte.repository.custom;


import com.querydsl.core.Tuple;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;

public interface OrdersRepositoryCustom {
    public Page<Orders> searchAllOrders(OrdersPageRequestDTO pageRequestDTO, Pageable pageable , String uid) throws ParseException;
}
