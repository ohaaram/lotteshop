package kr.co.lotte.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewImgDTO {

    private int rwno;
    private int rno;

    private String oName;
    private String sName;
}
