package kr.co.lotte.dto;

import kr.co.lotte.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {

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

    private LocalDateTime regDate;
    private LocalDateTime leaveDate;
    private int totalPoint;
    private int totalPrice;

    }


