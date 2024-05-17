package kr.co.lotte.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.Tuple;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
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
    private QProducts qProducts = QProducts.products;
    private QSubProducts subProducts = QSubProducts.subProducts;
    private QLike qLike = QLike.like;
    private final QProdImage qImages = QProdImage.prodImage;
    private final QSeller qSeller = QSeller.seller;

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (pageRequestDTO.getCate() != null && !pageRequestDTO.getCate().isEmpty()) {
            if (pageRequestDTO.getCate().equals("prodName")) {
                log.info("여기에 일단 들어와야해! - prodname");
                log.info("그러고 보니 keyword값은 ?"+pageRequestDTO.getKeyword());
                builder.and(qProducts.prodName.contains(pageRequestDTO.getKeyword()));
            }
            if (pageRequestDTO.getCate().equals("manufacture")) {
                log.info("여기는 manufacture");
                builder.and(qProducts.manufacture.contains(pageRequestDTO.getKeyword()));
            }
            if(pageRequestDTO.getCate().equals("sellerName")){
                log.info("여기는 sellerName");
                builder.and(qProducts.sellerName.contains(pageRequestDTO.getKeyword()));
            }
        }

        QueryResults<com.querydsl.core.Tuple> results = jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable, String uid) {

        BooleanBuilder builder = new BooleanBuilder();

        // 기본 검색어 조건
        builder.and(qProducts.sellerUid.eq(uid));

        if (!pageRequestDTO.getCate().isEmpty()) {
            if (pageRequestDTO.getCate().equals("prodName")) {
                builder.and(qProducts.prodName.contains(pageRequestDTO.getKeyword()));
            }
            if (pageRequestDTO.getCate().equals("manufacture")) {
                builder.and(qProducts.manufacture.contains(pageRequestDTO.getKeyword()));
            }
        }

        QueryResults<com.querydsl.core.Tuple> results = jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(builder)
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
                .select(qProducts, qImages, qSeller)
                .from(qImages)
                .join(qProducts)
                .on(qProducts.prodNo.eq(qImages.pNo))
                .join(qSeller)
                .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                .where(qProducts.prodNo.eq(prodno))
                .fetch();

        log.info("무슨 값들이 들어오는가? " + joinProduct);

        return joinProduct;

    }

    @Override
    public Tuple serachOnlyOne(int subProductNo) {
        Tuple results = jpaQueryFactory.select(qProducts, subProducts)
                .from(subProducts)
                .join(qProducts)
                .on(qProducts.prodNo.eq(subProducts.prodNo))
                .where(subProducts.subProdNo.eq(subProductNo)).fetchOne();

        return results;
    }

    @Override
    public Page<Tuple> searchAllProductsForList(MainProductsPageRequestDTO pageRequestDTO, Pageable pageable) {

        String cateName1 = pageRequestDTO.getCateName1();
        String cateName2 = pageRequestDTO.getCateName2();
        String cateName3 = pageRequestDTO.getCateName3();

        String cate = pageRequestDTO.getCate();
        String keyword = pageRequestDTO.getKeyword();


        JPAQuery<Tuple> query = null;
        OrderSpecifier<?> orderSpecifier;

        if (cateName1 != null && cateName2 != null && cateName3 != null && cateName1 != "" && cateName2 != "" && cateName3 != "") {
            query = jpaQueryFactory.select(qProducts,qSeller)
                    .from(qProducts)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .where(qProducts.cateName1.eq(cateName1).and(qProducts.cateName2.eq(cateName2).and(qProducts.cateName3.eq(cateName3))));

        } else if (cateName1 != null && cateName2 != null && cateName1 != "" && cateName2 != "") {
            query = jpaQueryFactory.select(qProducts,qSeller)
                    .from(qProducts)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .where(qProducts.cateName1.eq(cateName1).and(qProducts.cateName2.eq(cateName2)));



        } else if (cateName1 != null && cateName1 != "") {
            query = jpaQueryFactory.select(qProducts,qSeller)
                    .from(qProducts)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .where(qProducts.cateName1.eq(cateName1));


        }else if(keyword!=null && keyword!=""){//키워드가 들어오면 키워드로 상품 검색 실시
            query = jpaQueryFactory.select(qProducts,qSeller)
                    .from(qProducts)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .where(qProducts.prodName.contains(keyword));

        }else {
            query = jpaQueryFactory.select(qProducts,qSeller)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .from(qProducts);
        }

        // 동적으로 order by 절 추가
        if (pageRequestDTO.getCate() != null && !pageRequestDTO.getCate().isEmpty()) {

            log.info("cate값이 있을 때 동적으로 order by 절 추가 ");

            switch (pageRequestDTO.getCate()) {
                case "prodSold":
                    log.info("많이 팔린 순");
                    orderSpecifier = qProducts.prodSold.desc();//많이 팔린 순
                    break;

                case "lowPrice":
                    log.info("가격이 낮은 순");
                    orderSpecifier = qProducts.prodPrice.asc();//가격이 낮은 순
                    break;

                case "highPrice":
                    log.info("가격이 높은 순");
                    orderSpecifier = qProducts.prodPrice.desc();//가격이 높은 순
                    break;


                case "current":
                    log.info("최근 등록순");
                    orderSpecifier = qProducts.RegProdDate.desc();//최근 등록순
                    break;

                case "highAvg":
                    log.info("평균이 높은 순");
                    orderSpecifier = qProducts.avg.desc();//평균이 높은 순
                    break;

                case "highReview":
                    log.info("리뷰가 많은 순");
                    orderSpecifier = qProducts.reviews.size().desc();//리뷰가 많은 순
                    break;

                default:
                    log.info("기본값 : 상품명 오름차순");
                    orderSpecifier = qProducts.prodName.asc(); // 기본값: 상품명 오름차순
                    break;
            }
            query = query.orderBy(orderSpecifier);
        }

        // offset, limit 적용 및 실행
        QueryResults<Tuple> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<Tuple> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Products> searchAllProductsForSeller(MainProductsPageRequestDTO pageRequestDTO, Pageable pageable) {

        String seller = pageRequestDTO.getSeller();

        JPAQuery<Products> query = null;
        OrderSpecifier<?> orderSpecifier;
        String cate = pageRequestDTO.getCate();
            query = jpaQueryFactory.select(qProducts)
                    .from(qProducts)
                    .where(qProducts.sellerUid.eq(seller));
        // 동적으로 order by 절 추가
        if (pageRequestDTO.getCate() != null && !pageRequestDTO.getCate().isEmpty()) {

            log.info("cate값이 있을 때 동적으로 order by 절 추가 ");

            switch (pageRequestDTO.getCate()) {
                case "prodSold":
                    log.info("많이 팔린 순");
                    orderSpecifier = qProducts.prodSold.desc();//많이 팔린 순
                    break;

                case "lowPrice":
                    log.info("가격이 낮은 순");
                    orderSpecifier = qProducts.prodPrice.asc();//가격이 낮은 순
                    break;

                case "highPrice":
                    log.info("가격이 높은 순");
                    orderSpecifier = qProducts.prodPrice.desc();//가격이 높은 순
                    break;


                case "current":
                    log.info("최근 등록순");
                    orderSpecifier = qProducts.RegProdDate.desc();//최근 등록순
                    break;

                case "highAvg":
                    log.info("평균이 높은 순");
                    orderSpecifier = qProducts.avg.desc();//평균이 높은 순
                    break;

                case "highReview":
                    log.info("리뷰가 많은 순");
                    orderSpecifier = qProducts.reviews.size().desc();//리뷰가 많은 순
                    break;

                default:
                    log.info("기본값 : 상품명 오름차순");
                    orderSpecifier = qProducts.prodName.asc(); // 기본값: 상품명 오름차순
                    break;
            }
            query = query.orderBy(orderSpecifier);
        }


        // offset, limit 적용 및 실행
        QueryResults<Products> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Products> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<Like> searchAllLike(ProductsPageRequestDTO pageRequestDTO, Pageable pageable, String uid) {
        QueryResults<Like> results = jpaQueryFactory.select(qLike)
                .from(qLike)
                .where(qLike.userId.eq(uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Like> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    //상품 검색 기능
    public Page<Tuple> searchForProduct(ProductsPageRequestDTO pageRequestDTO, Pageable pageable) {
        QueryResults<Tuple> searchProducts = null;

        log.info("임플이야 - pageRequestDTO 검색조건 다 들어왔어? : " + pageRequestDTO);

        BooleanBuilder builder = new BooleanBuilder();

        // 기본 검색어 조건 추가
        builder.and(qProducts.prodName.contains(pageRequestDTO.getKeyword()));//이거 가능함. 테스트 완료

        log.info("체크박스를 클릭 했었어 : " + pageRequestDTO.getDetailCheckbox());

        log.info("디테일에 내가 적은게 잇어야해 : " + pageRequestDTO.getDetail());

        // 체크 박스에 상품명과 상품설명이 같이 체크된 경우
        if (pageRequestDTO.getDetailCheckbox() != null && pageRequestDTO.getEtcCheckbox() != null
        && pageRequestDTO.getDetailCheckbox() != "" && pageRequestDTO.getEtcCheckbox() != ""
        ) {
            if (pageRequestDTO.getDetailCheckbox().equals("상품명") && pageRequestDTO.getEtcCheckbox().equals("상품설명")) {
                log.info("여기는 상품명&상품설명 같이 체크");
                builder.and(qProducts.prodName.contains(pageRequestDTO.getDetail()));
                builder.and(qProducts.etc.contains(pageRequestDTO.getDetail()));
            }
        }

        // 체크 박스에 상품명만 체크된 경우
        if (pageRequestDTO.getDetailCheckbox() != null && pageRequestDTO.getEtcCheckbox() == null
        && pageRequestDTO.getDetailCheckbox() != "" && pageRequestDTO.getEtcCheckbox() != "") {
            if (pageRequestDTO.getDetailCheckbox().equals("상품명")) {
                log.info("여기는 상품명만 체크");
                builder.and(qProducts.prodName.contains(pageRequestDTO.getDetail()));
            }
        }
        //wow~ 이제 paginatio에도 다 넘겨주고~~
        // 체크 박스에 상품설명만 체크된 경우
        if (pageRequestDTO.getDetailCheckbox() == null && pageRequestDTO.getEtcCheckbox() != null) {
            if (pageRequestDTO.getEtcCheckbox().equals("상품설명")) {
                log.info("여기는 상품설명만 체크");
                builder.and(qProducts.etc.contains(pageRequestDTO.getDetail()));
            }
        }
        //체크 박스에 상품 가격만 체크된 경우
        if (pageRequestDTO.getPriceCheckbox() != null) {
            if (pageRequestDTO.getPriceCheckbox().equals("상품가격")) {

                log.info("여기는 상품가격 체크");

                int min = Integer.parseInt(pageRequestDTO.getMinPrice());
                int max = Integer.parseInt(pageRequestDTO.getMaxPrice());

                builder.and(qProducts.prodPrice.between(min, max));
            }
        }

        //체크박스를 선택하지 않았는데 값은 입력이 되있을 떄
        if (pageRequestDTO.getDetailCheckbox() == null
                && pageRequestDTO.getEtcCheckbox() == null
                && pageRequestDTO.getPriceCheckbox() == null
                ||pageRequestDTO.getDetailCheckbox() == ""
                && pageRequestDTO.getEtcCheckbox() == ""
                && pageRequestDTO.getPriceCheckbox() == "") {
            if (pageRequestDTO.getDetail() != null && !pageRequestDTO.getDetail().isEmpty()) {//(1.값이 검색어일때)

                log.info("체크박스를 선택하지 않았지만 값은 입력이 되어있을 때 - 값이 검색어 일 때");

                builder.and(qProducts.prodName.contains(pageRequestDTO.getDetail()));
            }
            if (pageRequestDTO.getMinPrice() != null && pageRequestDTO.getMaxPrice() != null
                    && !pageRequestDTO.getMaxPrice().isEmpty() && !pageRequestDTO.getMinPrice().isEmpty()) {//(2.값이 금액일때)

                log.info("체크박스를 선택하지 않았지만 값은 입력이 되어있을 때 - 값이 금액일 때");

                int min = Integer.parseInt(pageRequestDTO.getMinPrice());
                int max = Integer.parseInt(pageRequestDTO.getMaxPrice());

                builder.and(qProducts.prodPrice.between(min, max));
            }
        }

/*
        searchProducts = jpaQueryFactory
                    .select(qProducts,qSeller)
                    .from(qProducts)
                    .join(qSeller)
                    .on(qProducts.sellerUid.eq(qSeller.sellerUid))
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

 */
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(qProducts, qSeller)
                .from(qProducts)
                .join(qSeller).on(qProducts.sellerUid.eq(qSeller.sellerUid))
                .where(builder);

        // 동적으로 order by 절 추가
        if (pageRequestDTO.getCate() != null && !pageRequestDTO.getCate().isEmpty()) {

            log.info("cate값이 있을 때 동적으로 order by 절 추가 ");

            OrderSpecifier<?> orderSpecifier;
            switch (pageRequestDTO.getCate()) {
                case "prodSold":
                    log.info("많이 팔린 순");
                    orderSpecifier = qProducts.prodSold.desc();//많이 팔린 순
                    break;

                case "lowPrice":
                    log.info("가격이 낮은 순");
                    orderSpecifier = qProducts.prodPrice.asc();//가격이 낮은 순
                    break;

                case "highPrice":
                    log.info("가격이 높은 순");
                    orderSpecifier = qProducts.prodPrice.desc();//가격이 높은 순
                    break;


                case "current":
                    log.info("최근 등록순");
                    orderSpecifier = qProducts.RegProdDate.desc();//최근 등록순
                    break;

                case "highAvg":
                    log.info("평균이 높은 순");
                    orderSpecifier = qProducts.avg.desc();//평균이 높은 순
                    break;

                case "highReview":
                    log.info("리뷰가 많은 순");
                    orderSpecifier = qProducts.reviews.size().desc();//리뷰가 많은 순
                    break;

                default:
                    log.info("기본값 : 상품명 오름차순");
                    orderSpecifier = qProducts.prodName.asc(); // 기본값: 상품명 오름차순
                    break;
            }
            query = query.orderBy(orderSpecifier);
        }

        // offset, limit 적용 및 실행
        searchProducts = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        log.info("Impl - searchProducts 쿼리 결과물 : " + searchProducts.toString());

        List<Tuple> content = searchProducts.getResults();

        long total = searchProducts.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    //메인왼쪽 상단의 카테고리(히트상품.추천상품.최신상품.할인상품)

}
