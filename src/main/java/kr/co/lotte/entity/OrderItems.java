package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JoinFormula;

import javax.naming.Name;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="orderitems")

public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemNo;
    private int orderNo;
    private int prodNo;
    private int itemPrice;
    private int itemDiscount;
    private int itemCount;

    private String orderState;
    @CreationTimestamp
    private Date orderDate;

    private String excuse;

    @Transient
    private Products product;
}
