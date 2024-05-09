package kr.co.lotte.dto;

import kr.co.lotte.entity.CsQna;
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
public class CsQnaDTO {

    private int no;
    private int hit;
    private String cate1;
    private String cate2;
    private String catename;
    private String title;
    private String writer;
    private int file;
    private String content;
    private String comment;
    private String status;
    private LocalDateTime rdate;
    private String regip;

    private List<MultipartFile> files;

    private UserDTO user;
    private List<FileDTO> filelist;

    public CsQna toEntity(){
        CsQna csQna = new CsQna();
        csQna.setNo(no);
        csQna.setHit(hit);
        csQna.setCate1(cate1);
        csQna.setCate2(cate2);
        csQna.setCatename(catename);
        csQna.setTitle(title);
        csQna.setWriter(writer);
        csQna.setContent(content);
        csQna.setComment(comment);
        csQna.setStatus(status);
        csQna.setFile(file);
        csQna.setRdate(rdate);
        csQna.setRegip(regip);
        return csQna;
    }
}
