package kr.co.lotte.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProdImageDTO {
    private int iNo;
    private int pNo;
    private String oName;
    private String sName;
    private String Image240;//제목에 들어갈 이미지
    private String Image750;//내용에 들어갈 이미지


}
