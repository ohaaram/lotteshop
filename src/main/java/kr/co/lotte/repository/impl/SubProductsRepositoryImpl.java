package kr.co.lotte.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.MarketRepositoryCustom;
import kr.co.lotte.repository.custom.SubProductsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SubProductsRepositoryImpl implements SubProductsRepositoryCustom {

    //쿼리 DSL
    private final JPAQueryFactory queryFactory;
    private final QSubProducts subProducts = QSubProducts.subProducts;

    public List<SubProducts> findAllByProdNo(int prodno) {

        /*
        List<SubProducts> Option = queryFactory
                .select(subProducts)
                .from(subProducts)
                .where(subProducts.prodNo.eq(prodno))
                .fetch();

        log.info("result : "+ Option.size());
        log.info("result1 : "+Option);

        return Option;

         */

        List<String> uniqueColors = queryFactory
                .select(subProducts.color)
                .from(subProducts)
                .where(subProducts.prodNo.eq(prodno))
                .groupBy(subProducts.color)
                .fetch();

        // 각 컬러에 대한 다른 값들과 함께 조회
        List<SubProducts> uniqueSubProducts = new ArrayList<>();
        for (String color : uniqueColors) {
            SubProducts subProduct = queryFactory
                    .selectFrom(subProducts)
                    .where(subProducts.prodNo.eq(prodno).and(subProducts.color.eq(color)))
                    .fetchFirst(); // 컬러별로 첫 번째 데이터만 가져오거나 원하는 방식으로 수정
            uniqueSubProducts.add(subProduct);
        }

        return uniqueSubProducts;
    }

}
