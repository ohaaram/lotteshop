package kr.co.lotte.repository;

import kr.co.lotte.entity.Orders;
import kr.co.lotte.entity.Terms;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}