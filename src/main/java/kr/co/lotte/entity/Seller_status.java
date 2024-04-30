package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seller_status")
public class Seller_status {

    @Id
    private String sellerUid;//판매자 아이디
    private int orderCount;//주문건수
    private int totalPrice;//매출현황(총판매액)

}
