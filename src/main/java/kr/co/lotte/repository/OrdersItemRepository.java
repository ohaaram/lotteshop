package kr.co.lotte.repository;

import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersItemRepository extends JpaRepository<OrderItems, Integer> {
}