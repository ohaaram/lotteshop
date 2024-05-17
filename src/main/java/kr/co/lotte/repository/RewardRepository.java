package kr.co.lotte.repository;


import kr.co.lotte.entity.Points;
import kr.co.lotte.entity.Reward;
import kr.co.lotte.repository.custom.PointsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer>  {
    public Reward findByUidAndDate(String uid, String date);
}
