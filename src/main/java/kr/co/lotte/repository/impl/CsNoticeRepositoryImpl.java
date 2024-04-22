package kr.co.lotte.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.util.logging.Slf4j;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.CsNoticeRepository;
import kr.co.lotte.repository.custom.CsNoticeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CsNoticeRepositoryImpl implements CsNoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;



}
