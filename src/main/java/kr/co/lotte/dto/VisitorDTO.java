package kr.co.lotte.dto;

import kr.co.lotte.entity.Article;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorDTO {
    private Date visitorDate;
    private int visitCount;

}