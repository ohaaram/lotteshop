package kr.co.lotte.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.ReviewPageRequestDTO;
import kr.co.lotte.entity.QProducts;
import kr.co.lotte.entity.QReview;
import kr.co.lotte.entity.QUser;
import kr.co.lotte.entity.Review;
import kr.co.lotte.repository.custom.ReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QReview qReview = QReview.review;
    private final QUser qUser = QUser.user;
    private final QProducts qProducts = QProducts.products;

    // market/newview 리뷰 목록 조회
    @Override
    public Page<Tuple> selectReviewsAndNick(int prodno, ReviewPageRequestDTO pageRequestDTO, Pageable pageable){


        log.info("review_impl - prodno : "+prodno);
        log.info("review_impl - pageRequestDTO : "+pageRequestDTO);
        log.info("review_impl - pageable : "+pageable);

        // review 테이블과 User 테이블을 Join해서 리뷰 목록, 닉네임을 select
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qReview, qUser.nick)
                .from(qReview)
                .where(qReview.nproduct.prodNo.eq(prodno))
                .join(qUser)
                .on(qReview.uid.eq(qUser.uid))
                .orderBy(qReview.rno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        long total = results.getTotal();


        log.info("review_impl - total : "+total);

        List<Tuple> reviews = results.getResults();

        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(reviews, pageable, total);
    }
    // market/newview 리뷰 avg, sum, count(*) 조회
    public Tuple selectForRatio(int prodno){

        Tuple result = jpaQueryFactory
                .select(qReview.count(),
                        qReview.score.avg(),
                        qReview.score.sum())
                .from(qReview)
                .where(qReview.nproduct.prodNo.eq(prodno))
                .fetchOne();
        return result;
    }
    // market/newview 리뷰 score별 count 조회
    public List<Tuple> selectScoreCount(int prodno){

        // SELECT score, COUNT(*) FROM review WHERE prodno = ? GROUP BY score ORDER BY score;
        List<Tuple> results = jpaQueryFactory
                .select(qReview.score ,qReview.count())
                .from(qReview)
                .where(qReview.nproduct.prodNo.eq(prodno))
                .groupBy(qReview.score)
                .orderBy(qReview.score.asc())
                .fetch();
        log.info("리뷰 score별 count 조회 : " + results);
        return results;
    }
    public Page<Review> findByUid(String uid, ReviewPageRequestDTO pageRequestDTO, Pageable pageable){


        log.info("review_impl - uid : "+uid);
        log.info("review_impl - pageRequestDTO : "+pageRequestDTO);
        log.info("review_impl - pageable : "+pageable);

        //리뷰테이블의 결과를 출력 후 페이지네이션
        List<Review> results = jpaQueryFactory
                .select(qReview)
                .from(qReview)
                .where(qReview.uid.eq(uid))
                .fetch();

        long total = results.size();

        log.info("review_impl - total : "+total);

        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results, pageable, total);
    }
}
