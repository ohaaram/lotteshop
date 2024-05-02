package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name="NewTerms")

public class NewTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int intPk; //의미없는 pk값
    private String name; // state를 나타내는걸로 구매 / 판매 / 전자/ 위치/ 개인
    private String text;//약관
}