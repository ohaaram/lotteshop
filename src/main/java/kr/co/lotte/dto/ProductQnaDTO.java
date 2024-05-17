package kr.co.lotte.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQnaDTO {

    private int no;

    private String sellerUid;
    private int prodNo;
    private String uid;
    private String status1;
    private String cate;
    private String content;
    private String answer;
    private String status2;
    private String rdate;

}
