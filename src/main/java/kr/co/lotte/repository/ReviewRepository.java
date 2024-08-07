package kr.co.lotte.repository;


import kr.co.lotte.entity.Review;
import kr.co.lotte.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, ReviewRepositoryCustom {

    public List<Review> findTop5ByUidOrderByRdateDesc(String uid);

    int countByOrdernoAndNproductProdNoAndItemno(int orderno, int nproductProdNo, int itemno);

}
