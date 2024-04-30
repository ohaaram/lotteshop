package kr.co.lotte.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Seller_statusDTO {

    private String sellerUid;//판매자 아이디
    private long orderCount;//주문건수
    private Integer totalPrice;//매출현황(총판매액)

}
