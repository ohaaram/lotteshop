package kr.co.lotte.entity;

import jakarta.persistence.*;
import kr.co.lotte.dto.CsQnaDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name="csqna")
public class CsQna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Builder.Default
    private int hit=0;

    private String cate1;
    private String cate2;
    private String catename;
    private String title;
    private String writer;
    private String content;

    @Builder.Default
    private int file = 0;

    @CreationTimestamp
    private LocalDateTime rdate;

    private String regip;

    @OneToMany(mappedBy = "ano")
    private List<File> fileList;
    private String comment;
    private String status;

    public CsQnaDTO toDTO(){
        return CsQnaDTO.builder()
                .no(this.no)
                .hit(this.hit)
                .cate1(this.cate1)
                .cate2(this.cate2)
                .catename(this.catename)
                .title(this.title)
                .writer(this.writer)
                .content(this.content)
                .comment(this.comment)
                .status(this.status)
                .file(this.file)
                .rdate(this.rdate)
                .regip(this.regip)
                .build();
    }
}
