package kr.co.lotte.repository;

import kr.co.lotte.entity.Orders;
import kr.co.lotte.entity.Terms;
import kr.co.lotte.repository.custom.OrdersRepositoryCustom;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> , OrdersRepositoryCustom {
    public List<Orders> findTop3ByUserIdOrderByOrderNoDesc(String userId);
    public List<Orders> findALLByUserId(String userId);
}