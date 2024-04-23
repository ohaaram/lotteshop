package kr.co.lotte.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrdersDTO {
    private int orderNo;
    private String userId;
    private int orderTotalPrice;
    private String orderAddr;
    private int itemDiscount;
    private LocalDateTime orderDate;

    private String receiveName;
    private String receiveHp;
    private String memo;
    private String sendHp;
    private String sendName;

    private String zip;
    private String orderState;
    private String addr1;
    private String addr2;
    private String payment;
    private int point;
}
