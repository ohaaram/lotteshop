package kr.co.lotte.dto;

import com.querydsl.core.Tuple;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainProductsPageResponseDTO {

    private List<Products> dtoList;
    private List<Tuple> dtoList2;
    private String cateName1;
    private String cateName2;
    private String cateName3;
    private String cate;
    private String board;
    private String keyword;
    private String seller;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    @Builder
    public MainProductsPageResponseDTO (MainProductsPageRequestDTO pageRequestDTO, List<Products> dtoList, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;


        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        this.cateName1 = pageRequestDTO.getCateName1();
        this.cateName2 = pageRequestDTO.getCateName2();
        this.cateName3 = pageRequestDTO.getCateName3();
        this.keyword = pageRequestDTO.getKeyword();
        this.cate = pageRequestDTO.getCate();
        this.seller = pageRequestDTO.getSeller();
    }

    @Builder
    public MainProductsPageResponseDTO (MainProductsPageRequestDTO pageRequestDTO, int total, List<Tuple> dtoList){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList2 = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;


        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        this.cateName1 = pageRequestDTO.getCateName1();
        this.cateName2 = pageRequestDTO.getCateName2();
        this.cateName3 = pageRequestDTO.getCateName3();
        this.keyword = pageRequestDTO.getKeyword();
        this.cate = pageRequestDTO.getCate();
        this.seller = pageRequestDTO.getSeller();
    }



}