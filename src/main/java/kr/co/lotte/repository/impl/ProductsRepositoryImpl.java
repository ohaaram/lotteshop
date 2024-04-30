package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.Tuple;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepositoryCustom {
    private  QProducts qProducts = QProducts.products;
    private  QSubProducts subProducts = QSubProducts.subProducts;

    private final QProdImage qImages = QProdImage.prodImage;
    private final QSeller qSeller = QSeller.seller;

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable) {
        QueryResults<com.querydsl.core.Tuple> results = jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable, String uid) {
        QueryResults<com.querydsl.core.Tuple> results = jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(qProducts.sellerUid.eq(uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    //(product/view) 출력
    @Override
    public List<Tuple> selectProduct(int prodno) {
        // select * from `product` as a join `images` as b on a.prodno = b.prodno where a`prodno` = ?
        //SELECT * FROM `prodimage`AS a JOIN `products` AS b ON a.pNo = b.prodNo JOIN `seller`AS c ON b.sellerUid=c.sellerUid WHERE b.prodNo=1;
        List<Tuple> joinProduct = jpaQueryFactory
                .select(qProducts, qImages,qSeller)
                .from(qImages)
                .join(qProducts)
                .on(qProducts.prodNo.eq(qImages.pNo))
                .join(qSeller)
                .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                .where(qProducts.prodNo.eq(prodno))
                .fetch();

        log.info("무슨 값들이 들어오는가? " +joinProduct);

        return joinProduct;

    }

    @Override
    public Tuple serachOnlyOne(int subProductNo) {
      Tuple results =  jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(subProducts.subProdNo.eq(subProductNo)).fetchOne();

        return results;
    }

    @Override
    public Page<Products> searchAllProductsForList(MainProductsPageRequestDTO pageRequestDTO, Pageable pageable) {
         String cateName1 = pageRequestDTO.getCateName1();
        String cateName2 = pageRequestDTO.getCateName2();
        String cateName3 = pageRequestDTO.getCateName3();
        QueryResults<Products> results =null;
        if(cateName1 != null && cateName2 != null && cateName3 != null && cateName1 != "" && cateName2 != "" && cateName3 != "" ) {
            results = jpaQueryFactory.select(qProducts)
                    .from(qProducts)
                    .where(qProducts.cateName1.eq(cateName1).and(qProducts.cateName2.eq(cateName2).and(qProducts.cateName3.eq(cateName3))))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }else if(cateName1 != null && cateName2 != null && cateName1 != "" && cateName2 != "") {
            results = jpaQueryFactory.select(qProducts)
                    .from(qProducts)
                    .where(qProducts.cateName1.eq(cateName1).and(qProducts.cateName2.eq(cateName2)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else if(cateName1 != null && cateName1 != "") {
            results = jpaQueryFactory.select(qProducts)
                    .from(qProducts)
                    .where(qProducts.cateName1.eq(cateName1))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else{
            results = jpaQueryFactory.select(qProducts)
                    .from(qProducts)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        List<Products> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}
