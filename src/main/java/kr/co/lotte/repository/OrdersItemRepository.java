package kr.co.lotte.repository;

import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersItemRepository extends JpaRepository<OrderItems, Integer> {
    List<OrderItems> findAllByOrderNo(int orderNo);

}