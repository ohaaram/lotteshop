package kr.co.lotte.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CategoriesDTO {

    private int cateNo;
    private String cateName;
    private String secondCateName;
    private String ThirdCateName;

}
