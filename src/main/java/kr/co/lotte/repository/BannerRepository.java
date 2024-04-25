package kr.co.lotte.repository;


import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    public List<Banner> findByPosition(String MAIN1);

    public void deleteByBannerNo(int BNo);//배너 번호를 이용하여 삭제

    Long countByPositionAndStatus(String position, int status);
}
