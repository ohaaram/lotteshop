package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="products")

public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodNo;
    private int cateNo;
    private String sellerUid;
    private String prodName;

    private int prodDiscount;

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String manufacture;
    private int searchCount; //상품조회

    private int recount;
    private int point;
    private String etc;
    private int delivery;

    @CreationTimestamp
    private LocalDateTime RegProdDate;

    @Transient
    private String cateName;

    private String cateName1;
    private String cateName2;
    private String cateName3;
    private String prodState;
    private Integer prodPrice;
    private String prodTax;
    private String prodReceipt;
    private String prodSa;
    private String prodWonsan;

    private String sellerName;

    @Transient
    private SubProducts subProducts;
}
