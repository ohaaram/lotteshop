package kr.co.lotte.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.entity.QProdImage;
import kr.co.lotte.entity.QProducts;
import kr.co.lotte.entity.QSeller;
import kr.co.lotte.entity.QSubProducts;
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

    private QProducts qProducts = QProducts.products;
    private QSubProducts subProducts = QSubProducts.subProducts;

    private final QProdImage qImages = QProdImage.prodImage;
    private final QSeller qSeller = QSeller.seller;

    @Override
    public int countstatus(String position) {

        Tuple results =  jpaQueryFactory.select()
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(subProducts.subProdNo.eq(subProductNo)).fetchOne();




        return 0;
    }
}
