package kr.co.lotte.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@DynamicInsert
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    private String sellerUid;
    private String sellerPass;
    private String sellerName;
    private String sellerCEO;
    private String sellerSaNumber;
    private String sellerTongNumber;
    private String sellerHp;
    private String sellerFax;
    private String sellerEmail;
    private String sellerZip;
    private String sellerAddr1;
    private String sellerAddr2;
    private String sellerDname;
    private String sellerDHp;
    private String role;
    private String grade;

    @Transient
    private Long orderCount;//판매자 마다 상품을 몇개 팔았는지

    @Transient
    private Integer totalPrice;//총 판매금액

}
