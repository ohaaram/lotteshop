package kr.co.lotte.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="recruit")
public class Recruit {

    @Id
    private int r_no;
    private String r_title;
    private String r_content;
    private String r_cate;//it?광고사업?
    private String career;//신입/경력
    private String employment_type;//정규직
    private String period;//상시채용

}
