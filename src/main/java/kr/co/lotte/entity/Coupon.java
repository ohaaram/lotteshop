package kr.co.lotte.entity;

import jakarta.persistence.*;
import kr.co.lotte.dto.ArticleDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponCode;
    private String couponName;
    private  int discount;
    private  int min;
    private  int max;
    private String endDate;
    private  int download;
    private  int state;

}