package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Setter
@Entity
@Table(name="points")

public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pointNo;
    private String userId;
    private int point;
    private String pointDesc;
    @CreationTimestamp
    private LocalDateTime pointDate;

    private LocalDateTime endDateTime;

    private int orderNo;

    private String state;
}
