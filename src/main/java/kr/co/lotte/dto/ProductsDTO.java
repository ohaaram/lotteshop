package kr.co.lotte.dto;

import jakarta.persistence.Transient;
import kr.co.lotte.entity.SubProducts;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductsDTO {
    private int prodNo;
    private int cateNo;
    private String sellerUid;
    private String prodName;
    private int prodStock;
    private int prodSold;
    private int prodDiscount;



    private MultipartFile multImage1;
    private MultipartFile multImage2;
    private MultipartFile multImage3;
    private MultipartFile multImage4;


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

    private LocalDateTime RegProdDate;

    private String cateName;

    private String cateName1;
    private String cateName2;
    private String cateName3;
    private String prodState;
    private String prodTax;
    private int prodPrice;
    private String prodReceipt;
    private String prodSa;
    private String prodWonsan;

    private String sellerName;
    private List<SubProducts> list;
}
