package kr.co.lotte.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RecruitDTO {

    private int r_no;
    private String r_title;
    private String r_content;
    private String r_cate;//it?광고사업?
    private String career;//신입/경력
    private String employment_type;//정규직
    private String period;//상시채용

}
