package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String uid;
    private String comment;
    private int score;
    private String thumbnail;

    @Transient
    private String nick;

    private String prodname;

    private int orderno;
    private int itemno;

    private String prodoption;//리뷰쓸때 옵션 가져오기

    @CreationTimestamp
    private LocalDateTime rdate;

    @ManyToOne
    @JoinColumn(name = "prodNo")
    private Products nproduct;

    // 수정된 toString() 메서드
    @Override
    public String toString() {
        return "Review{" +
                "rno=" + rno +
                ", uid='" + uid + '\'' +
                ", comment='" + comment + '\'' +
                ", score=" + score +
                ", thumbnail='" + thumbnail + '\'' +
                ", nick='" + nick + '\'' +
                ", prodname='" + prodname + '\'' +
                ", prodoption='" + prodoption + '\'' +
                ", orderno=" + orderno +
                ", itemno=" + itemno +
                ", rdate=" + rdate +
                ", prodNo=" + (nproduct != null ? nproduct.getProdNo() : null) +
                '}';
    }
}