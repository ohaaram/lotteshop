package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "visitor")
public class Visitor {
    @Id
    private String visitorDate;
    private int visitCount;
}
