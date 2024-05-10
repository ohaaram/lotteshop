package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Setter
@Table(name="carts")

public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartNo;
    private String userId;
    private int prodNo;
    private int cartProdCount;

    @CreationTimestamp
    private LocalDateTime cartProdDate;

    private int prodPrice;
    private int prodDiscount;

}
