package kr.co.lotte.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageRequestDTO {

    @Builder.Default
    private  int no =1;

    @Builder.Default
    private  int pg=1;

    @Builder.Default
    private  int size =10;

    private String keyword;//검색어

    private String detail;//상세검색어(상품명 또는 상품 설명)

    private String minPrice;//최소값
    private String maxPrice;//최대값

    private String detailCheckbox;//상세 상품명 체크박스 클릭되었나?
    private String etcCheckbox;//상품 설명 체크박스 클릭되었나?
    private String priceCheckbox;
    private String cate;
    private String board;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }
}