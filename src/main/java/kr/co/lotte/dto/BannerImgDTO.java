package kr.co.lotte.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerImgDTO {

    private int ino;
    private int bno;
    private String oName;
    private String sName;
}
