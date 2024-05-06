package kr.co.lotte.repository.cs;

import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.custom.CsNoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsNoticeRepository extends JpaRepository<CsNotice, Integer>{

    List<CsNotice> findAll();
    CsNotice findFirstByCate2(String cate2);


}
