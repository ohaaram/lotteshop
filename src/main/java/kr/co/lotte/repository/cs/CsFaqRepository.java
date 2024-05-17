package kr.co.lotte.repository.cs;

import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.repository.custom.CsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CsFaqRepository extends JpaRepository<CsFaq, Integer> , CsRepositoryCustom {

    Page<CsFaq> findAll(Pageable pageable);
    List<CsFaq> findByCate1(String cate1);
    Page<CsFaq> findByCate1AndCate2(String cate1, String cate2, Pageable pageable);
    CsFaq findFirstByCate2(String cate2);

}
