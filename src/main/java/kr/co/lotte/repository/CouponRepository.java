package kr.co.lotte.repository;

import kr.co.lotte.entity.BannerImg;
import kr.co.lotte.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer>{
    List<Coupon> findAllByState(int state);

}
