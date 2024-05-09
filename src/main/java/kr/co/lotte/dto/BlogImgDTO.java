package kr.co.lotte.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlogImgDTO {

    private int bwno;
    private int bno;

    private String oName;
    private String sName;

}
