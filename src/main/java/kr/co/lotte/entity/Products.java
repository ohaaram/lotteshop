package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private int prodSold; //판매수량
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

    private int hit;

    private double avg;//리뷰의 평균값

    @Transient
    private SubProducts subProducts;

    @Transient
    private int likeState;

    @Transient
    private Seller seller;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prodNo") // Review 테이블의 prodno 열과 조인합니다.
    private List<Review> reviews = new ArrayList<>();

    // 수정된 toString() 메서드
    @Override
    public String toString() {
        return "Products{" +
                "prodNo=" + prodNo +
                ", cateNo=" + cateNo +
                ", prodSold=" + prodSold +
                ", sellerUid='" + sellerUid + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodDiscount=" + prodDiscount +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", searchCount=" + searchCount +
                ", recount=" + recount +
                ", point=" + point +
                ", etc='" + etc + '\'' +
                ", delivery=" + delivery +
                ", RegProdDate=" + RegProdDate +
                ", cateName='" + cateName + '\'' +
                ", cateName1='" + cateName1 + '\'' +
                ", cateName2='" + cateName2 + '\'' +
                ", cateName3='" + cateName3 + '\'' +
                ", prodState='" + prodState + '\'' +
                ", prodPrice=" + prodPrice +
                ", prodTax='" + prodTax + '\'' +
                ", prodReceipt='" + prodReceipt + '\'' +
                ", prodSa='" + prodSa + '\'' +
                ", prodWonsan='" + prodWonsan + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", subProducts=" + subProducts +
                ", likeState=" + likeState +
                ", seller=" + seller +
                ", reviews=" + reviews +
                '}';
    }


    public void calculateAvgRating() {
        if (reviews != null && !reviews.isEmpty()) {
            double totalScore = 0.0;
            for (Review review : reviews) {
                totalScore += review.getScore();
            }
            avg = totalScore / reviews.size();
        } else {
            avg = 0.0; // 리뷰가 없을 경우 0으로 설정
        }
    }
}
