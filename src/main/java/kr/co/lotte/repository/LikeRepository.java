package kr.co.lotte.repository;


import kr.co.lotte.entity.Like;
import kr.co.lotte.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    public List findByUserIdAndProdNo(String userId, int prodNo);

    public void deleteByUserIdAndProdNo(String userId, int prodNo);
}
