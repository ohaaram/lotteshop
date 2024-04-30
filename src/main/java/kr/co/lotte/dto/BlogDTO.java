package kr.co.lotte.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {

    private int bno;
    private String title;
    private String content;
    private String cate;
    private String author;
    private LocalDateTime date;
    private String oneLine;
    private String iamges;

}
