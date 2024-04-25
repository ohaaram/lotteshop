package kr.co.lotte.dto;

import kr.co.lotte.entity.CsFaq;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString @Builder
public class CsFaqDTO {

    private int no;
    private String cate1;
    private String cate2;
    private String catename;
    private String title;
    private String content;

    public CsFaq toEntity(){
        CsFaq article = new CsFaq();
        article.setNo(no);
        article.setCate1(cate1);
        article.setCate2(cate2);
        article.setCatename(catename);
        article.setTitle(title);
        article.setContent(content);

        return article;
    }

}
