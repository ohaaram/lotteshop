package kr.co.lotte.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewDTO {
    private int rno;
    private String uid;
    private String comment;
    private int prodNo;
    private int score;

    private MultipartFile multImage1;

    private String thumbnail;

    private LocalDateTime rdate;

    // 리뷰 조회용
    private String nick;

    private String prodname;

    private int orderno;
    private int itemno;
    private String prodoption;//옵션(color+size)

}