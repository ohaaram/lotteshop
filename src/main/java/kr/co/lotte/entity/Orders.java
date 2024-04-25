package kr.co.lotte.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Setter
@Table(name="orders")

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderNo;
    private String userId;
    private int orderTotalPrice;
    private String orderAddr;
    private int itemDiscount;
    @CreationTimestamp
    private Date orderDate;


    private String zip;
    private String orderState;
    private String addr1;
    private String addr2;
    private String payment;
    private int point;

}
