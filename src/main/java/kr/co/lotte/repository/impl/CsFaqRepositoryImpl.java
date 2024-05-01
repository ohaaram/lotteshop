package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.CsRepositoryCustom;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CsFaqRepositoryImpl implements CsRepositoryCustom {
    private  QCsFaq csFaq = QCsFaq.csFaq;
    private  QCsNotice csNotice = QCsNotice.csNotice;

    private final QProdImage qImages = QProdImage.prodImage;
    private final QSeller qSeller = QSeller.seller;

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    //faq 페이징처리
    @Override
    public Page<CsFaq> searchAllCsFaq(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable) {
        String cate1 = pageRequestDTO.getCate1();
        String cate2 = pageRequestDTO.getCate2();
        QueryResults<CsFaq> results =null;
        if(cate1 != null && cate1 != ""){
            results = jpaQueryFactory.select(csFaq)
                    .from(csFaq)
                    .where(csFaq.cate1.eq(cate1).and(csFaq.cate2.eq(cate2)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else{
            results = jpaQueryFactory.select(csFaq)
                    .from(csFaq)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        List<CsFaq> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    //notice 페이징처리
    @Override
    public Page<CsNotice> searchAllCsNotice(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable) {
        QueryResults<CsNotice> results =null;
        String cate1 = pageRequestDTO.getCate1();
        if(cate1 != null && cate1 != ""){
            results = jpaQueryFactory.select( csNotice)
                    .from( csNotice)
                    .where(csNotice.cate1.eq(cate1))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }else{
            results = jpaQueryFactory.select( csNotice)
                    .from( csNotice)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
    List<CsNotice> content = results.getResults();
    long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}
