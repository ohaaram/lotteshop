package kr.co.lotte.repository;

import kr.co.lotte.entity.Coupon;
import kr.co.lotte.entity.DownloadCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DownloadCouponRepository extends JpaRepository<DownloadCoupon, Integer>{
    public DownloadCoupon findByCouponCodeAndUid(int couponCode, String uid);
    public List<DownloadCoupon> findAllByUid(String uid);
    public List<DownloadCoupon> findAllByStateAndUid(int state, String uid);
}
