package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="csfaq")
public class CsFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String cate;
    private String title;
    private String content;

}
