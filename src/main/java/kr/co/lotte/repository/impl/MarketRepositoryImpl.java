package kr.co.lotte.repository.impl;

import kr.co.lotte.repository.custom.MarketRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepositoryCustom {



   /*

    // market/view에서 장바구니에 품목 추가
    @Override
    @Transactional
    public Integer addProductForCart(String uid, int prodno, int prodCount){
        Integer cartNo = jpaQueryFactory
                            .select(qCart.cartNo)
                            .from(qCart)
                            .where(qCart.uid.eq(uid))
                            .fetchOne();

        List<Cart_product> result = jpaQueryFactory
                                    .selectFrom(qCart_product)
                                    .where(qCart_product.cartNo.eq(cartNo).and(qCart_product.prodNo.eq(prodno)))
                                    .fetch();

        if (result.isEmpty()){
            return cartNo;
        }else {
            return -1; // 이미 존재하는 상품
        }
    }

    */
}
