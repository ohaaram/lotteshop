package kr.co.lotte.dto;

import kr.co.lotte.entity.CsFaq;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString @Builder
public class CsFaqDTO {

    private int no;
    private String cate;
    private String title;
    private String content;

    public CsFaq toEntity(){
        CsFaq article = new CsFaq();
        article.setNo(no);
        article.setCate(cate);
        article.setTitle(title);
        article.setContent(content);

        return article;
    }

}
