package kr.co.lotte.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadCouponDTO {

    private int pk;
    private  int couponCode;
    private  String uid;

}