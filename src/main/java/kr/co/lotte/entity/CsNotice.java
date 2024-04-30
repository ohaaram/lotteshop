package kr.co.lotte.entity;

import jakarta.persistence.*;
import kr.co.lotte.dto.CsNoticeDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name="csnotice")
public class CsNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Builder.Default
    private int hit = 0;

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

    @OneToMany(mappedBy = "ano") //mappedBy는 매핑 되는 엔티티(테이블)의 FK 컬럼 지정
    private List<File> fileList;

    public CsNoticeDTO toDTO (){
        return CsNoticeDTO.builder()
                .no(this.no)
                .hit(this.hit)
                .cate1(this.cate1)
                .cate2(this.cate2)
                .catename(this.catename)
                .title(this.title)
                .writer(this.writer)
                .content(this.content)
                .file(this.file)
                .rdate(this.rdate)
                .regip(this.regip)
                .build();
    }

}
