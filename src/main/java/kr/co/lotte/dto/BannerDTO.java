package kr.co.lotte.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {

    private int bannerNo;
    private String name;

    private MultipartFile multImage1;
    private MultipartFile multImage2;


    private String img_1200;//이미지 파일(1200*80)
    private String img_985;//이미지 파일(985*447)
    private String img_456;//이미지 파일(456*60)
    private String img_425;//이미지 파일(425*260)
    private String img_810;//이미지 파일(810*86)


    private int hit;
    private String color;
    private String link;
    private String status;
    private String position;
    private String d_begin;//배너가 노출되는 시작날짜
    private String d_end;//배너가 노출되는 끝날짜
    private String t_begin;//배너 노출이 시작되는 시간
    private String t_end;//배너 노출이 끝나는 시간

}
