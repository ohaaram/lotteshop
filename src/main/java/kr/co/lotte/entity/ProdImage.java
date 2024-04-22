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
@Table(name = "prodimage")
public class ProdImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iNo;
    private int pNo;
    private String oName;
    private String sName;
    private String Image240;//제목에 들어갈 이미지
    private String Image750;//내용에 들어갈 이미지



}