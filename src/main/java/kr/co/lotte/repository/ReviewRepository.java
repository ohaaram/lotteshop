package kr.co.lotte.repository;


import kr.co.lotte.entity.Review;
import kr.co.lotte.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, ReviewRepositoryCustom {

}
