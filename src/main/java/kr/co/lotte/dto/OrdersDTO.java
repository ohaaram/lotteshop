package kr.co.lotte.dto;

import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.SubProducts;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    private Date orderDate;

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

    private List<OrderItems> orderItems;

    //추가된 친구들쓰~
    private  int delivery;
    private  int couponCode;
    private  int couponDiscount;

}
