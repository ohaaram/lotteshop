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
@Table(name = "downloadCoupon")
public class DownloadCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private  int couponCode;
    private  String uid;
    private  int state;

}