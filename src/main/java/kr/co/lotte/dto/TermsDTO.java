package kr.co.lotte.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermsDTO {

    int intPk;
    private String terms;
    private String terms2;
    private String privacy;
    private String sms;
}