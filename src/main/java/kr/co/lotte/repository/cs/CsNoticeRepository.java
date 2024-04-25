package kr.co.lotte.repository.cs;

import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.custom.CsNoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsNoticeRepository extends JpaRepository<CsNotice, Integer>, CsNoticeRepositoryCustom {
}
