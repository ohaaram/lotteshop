package kr.co.lotte.dto;

import kr.co.lotte.entity.Like;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductsPageResponseDTO {

    private List<Products> dtoList;
    private List<SubProducts> dtoLists;
    private List<Like> dtoListLikes;
    private  String keyword;
    private String detail;
    private String minPrice;
    private String maxPrice;
    private String detailCheckbox;//상세 상품명 체크박스 클릭되었나?

    private String etcCheckbox;//상품 설명 체크박스 클릭되었나?

    private String priceCheckbox;//가격 체크박스 클릭되었나?

    private String cate;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    @Builder
    public ProductsPageResponseDTO(ProductsPageRequestDTO pageRequestDTO, List<Products> dtoList, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.keyword=pageRequestDTO.getKeyword();
        this.detail=pageRequestDTO.getDetail();
        this.minPrice= pageRequestDTO.getMinPrice();
        this.maxPrice= pageRequestDTO.getMaxPrice();
        this.detailCheckbox = pageRequestDTO.getDetailCheckbox();
        this.etcCheckbox = pageRequestDTO.getEtcCheckbox();
        this.priceCheckbox = pageRequestDTO.getPriceCheckbox();
        this.cate = pageRequestDTO.getCate();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

    @Builder
    public ProductsPageResponseDTO(ProductsPageRequestDTO pageRequestDTO, int total, List<SubProducts> dtoList){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoLists = dtoList;
        this.cate = pageRequestDTO.getCate();
        this.keyword = pageRequestDTO.getKeyword();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

    @Builder
    public ProductsPageResponseDTO(List<Like> dtoList, ProductsPageRequestDTO pageRequestDTO, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoListLikes = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }


}