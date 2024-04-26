package kr.co.lotte.repository.cs;

import kr.co.lotte.entity.CsFaq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CsFaqRepository extends JpaRepository<CsFaq, Integer> {

    Page<CsFaq> findAll(Pageable pageable);
    //List<CsFaq> findByCate(String cate);
    //Optional<CsFaq> findByNo(int no);

    List<CsFaq> findByCate1(String cate1);

}
