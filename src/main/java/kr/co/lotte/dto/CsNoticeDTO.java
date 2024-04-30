package kr.co.lotte.dto;

import kr.co.lotte.entity.CsNotice;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsNoticeDTO {

    private int no;
    private int hit;
    private String cate1;
    private String cate2;
    private String catename;
    private String title;
    private String writer;
    private String content;
    private int file;
    private LocalDateTime rdate;
    private String regip;

    private List<MultipartFile> files;

    private UserDTO user;
    private List<FileDTO> filelist;

    public CsNotice toEntity(){
        CsNotice csNotice = new CsNotice();
        csNotice.setNo(no);
        csNotice.setHit(hit);
        csNotice.setCate1(cate1);
        csNotice.setCate2(cate2);
        csNotice.setCatename(catename);
        csNotice.setTitle(title);
        csNotice.setWriter(writer);
        csNotice.setContent(content);
        csNotice.setFile(file);
        csNotice.setRdate(rdate);
        csNotice.setRegip(regip);

        return csNotice;
    }
}
