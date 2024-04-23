package kr.co.lotte.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserUpdateDTO {


    private String pass;
    private String email;
    private String hp;
    private String zip;
    private String addr1;
    private String addr2;

}
