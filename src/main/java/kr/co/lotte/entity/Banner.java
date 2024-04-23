package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "banner")
public class Banner {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bannerNo;
    private String name;//배너 이름

    private String img_1200;//이미지 파일(1200*80)
    private String img_985;//이미지 파일(985*447)
    private String img_456;//이미지 파일(456*60)
    private String img_425;//이미지 파일(425*260)
    private String img_810;//이미지 파일(810*86)


    private int hit;//조회 수
    private String color;//배너 백그라운드
    private String link;//배너링크
    private String status;
    private String position;//배너위치
    private String d_begin;//배너가 노출되는 시작날짜
    private String d_end;//배너가 노출되는 끝날짜
    private String t_begin;//배너 노출이 시작되는 시간
    private String t_end;//배너 노출이 끝나는 시간

}
