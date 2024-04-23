package kr.co.lotte.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.BannerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private QBanner qBanner = QBanner.banner;


    @Override
    public Long countByPositionAndStatus(String position,int status) {

        //position이 일치하면서 status가 1인 status의 수
        Long results = jpaQueryFactory
                .select(qBanner.status.count())
                .from(qBanner)
                .where(qBanner.position.eq(position).and(qBanner.status.eq(String.valueOf(status))))
                .fetchOne();

        return results;
    }
}
