package kr.co.lotte.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SubProductsDTO {

    private int subProdNo;
    private int prodPrice;
    private int prodStock;
    private int prodSold;
    private int prodNo;

    private String size;
    private String color;

}
