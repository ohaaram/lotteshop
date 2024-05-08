package kr.co.lotte.dto;

import kr.co.lotte.entity.Article;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponDTO {

    private int couponCode;
    private String couponName;
    private  int discount;
    private  int min;
    private  int max;
    private String endDate;
    private  int download;

}