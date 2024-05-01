package kr.co.lotte.repository.impl.cs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.util.logging.Slf4j;
import kr.co.lotte.repository.custom.CsNoticeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CsNoticeRepositoryImpl implements CsNoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

}
