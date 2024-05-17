package kr.co.lotte.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.SellerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private QProducts qProducts = QProducts.products;
    private QSubProducts subProducts = QSubProducts.subProducts;
    private QOrderItems orderItems = QOrderItems.orderItems;
    private QSeller qSeller = QSeller.seller;

    public Page<Tuple> seller_status(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable) {

        long total = 0;
/*
        QueryResults<Tuple> result = jpaQueryFactory
                .select(qProducts.sellerUid, orderItems.itemNo.count(), orderItems.itemPrice.multiply(orderItems.itemCount).sum())
                .from(orderItems)
                .join(subProducts)
                .on(orderItems.prodNo.eq(subProducts.subProdNo))
                .join(qProducts)
                .on(subProducts.prodNo.eq(qProducts.prodNo))
                .groupBy(qProducts.sellerUid)
                .fetchResults();

 */

        QueryResults<Tuple> result = jpaQueryFactory
                .select(qSeller.sellerUid, orderItems.itemNo.count(), orderItems.itemPrice.multiply(orderItems.itemCount).sum(),qSeller.role)
                .from(qSeller)
                .leftJoin(qProducts)
                .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                .leftJoin(subProducts)
                .on(subProducts.prodNo.eq(qProducts.prodNo))
                .leftJoin(orderItems)
                .on(subProducts.subProdNo.eq(orderItems.prodNo))
                .groupBy(qSeller.sellerUid)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<Tuple> status = result.getResults();
        total = result.getResults().size();

        log.info("total : " + total);

        //주문건수&매출현황 출력
        log.info("seller_status - 주문건수와 매출 현황 : " + result.toString());

        //페이지 처리용 page 객체 리턴
        return new PageImpl<>(status, pageable, total);
    }

    //아직 허가 받지 못한 셀러들의 리스트
    public List<Seller> waitingSellers() {

        List<Seller> result = jpaQueryFactory
                .select(qSeller)
                .from(qSeller)
                .where(qSeller.role.eq("TEMP"))
                .fetch();

        return result;
    }

}
