package kr.co.lotte.repository;

import kr.co.lotte.entity.ProductQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQnaRepository extends JpaRepository<ProductQna, Integer> {

}
