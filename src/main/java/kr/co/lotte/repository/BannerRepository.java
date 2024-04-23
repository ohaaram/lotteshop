package kr.co.lotte.repository;


import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    List<Banner> findByPosition(String position);

    void deleteByBannerNo(int bannerNo);

    Long countByPositionAndStatus(String position, int status); // 수정된 메서드 시그니처
}
