package kr.co.lotte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "productqna")
public class ProductQna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String sellerUid;
    private int prodNo;
    private String uid;
    private String status1;
    private String cate;
    private String content;
    private String answer;
    private String status2;

    @CreationTimestamp
    private String rdate;

    @Transient
    private String productName;

    public ProductQna toEntity(){
        ProductQna productQna = new ProductQna();
        productQna.setNo(no);
        productQna.setSellerUid(sellerUid);
        productQna.setProdNo(prodNo);
        productQna.setUid(uid);
        productQna.setStatus1(status1);
        productQna.setCate(cate);
        productQna.setContent(content);
        productQna.setAnswer(answer);
        productQna.setStatus2(status2);
        productQna.setRdate(rdate);
        return productQna;
    }
}
