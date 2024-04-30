package kr.co.lotte.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.StatusPageResponseDTO;
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
public class Seller_statusRepositoryImpl implements SellerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private  QProducts qProducts = QProducts.products;
    private  QSubProducts subProducts = QSubProducts.subProducts;
    private QOrderItems orderItems = QOrderItems.orderItems;

    public Page<Tuple> seller_status(CsFaqPageRequestDTO pageRequestDTO,Pageable pageable){

        long total = 0;

        QueryResults<Tuple> result = jpaQueryFactory.select(qProducts.sellerUid, orderItems.itemNo.count(),orderItems.itemPrice.multiply(orderItems.itemCount).sum())
                .from(orderItems)
                .join(subProducts)
                .on(orderItems.prodNo.eq(subProducts.subProdNo))
                .join(qProducts)
                .on(subProducts.prodNo.eq(qProducts.prodNo))
                .groupBy(qProducts.sellerUid)
                .fetchResults();

        List<Tuple> status = result.getResults();
        total = result.getResults().size();

        log.info("total : " +total);

        //주문건수&매출현황 출력
        log.info("seller_status - 주문건수와 매출 현황 : "+ result.toString());

        //페이지 처리용 page 객체 리턴
        return new PageImpl<>(status,pageable,total);
    }

}
