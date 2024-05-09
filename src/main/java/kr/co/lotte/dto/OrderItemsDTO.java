package kr.co.lotte.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderItemsDTO {
    private int itemNo;
    private int orderNo;
    private int prodNo;
    private int itemPrice;
    private int itemDiscount;
    private int itemCount;

    private String excuse;

    private String orderState;
    private Date orderDate;
}
