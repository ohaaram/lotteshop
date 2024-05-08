package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="subProducts")

public class SubProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subProdNo;

    private int prodNo;
    private int prodPrice;
    private int prodStock;
    private int prodSold;


    private String size;
    private String color;

    @Transient
    private Products products;
    @Transient
    private String prodName;

    @Transient
    private int count;

}
