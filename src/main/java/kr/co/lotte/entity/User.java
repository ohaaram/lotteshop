package kr.co.lotte.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    private String uid;
    private String pass;
    private String name;
    private String nick;
    private String email;
    private String hp;
    private String role;
    private String zip;
    private String addr1;
    private String addr2;
    private String regip;
    private String sms;
    private String agree;
    private String provider;
    private String grade;
    private String gender;
    private int status;

    @CreationTimestamp
    private LocalDateTime regDate;
    private LocalDateTime leaveDate;
    private int totalPoint;
    private int totalPrice;

}