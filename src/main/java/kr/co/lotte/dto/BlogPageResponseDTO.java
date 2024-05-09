package kr.co.lotte.dto;


import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.Products;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogPageResponseDTO {

    private List<Blog> dtoList;
    private String cate;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    @Builder
    public BlogPageResponseDTO (BlogPageRequestDTO pageRequestDTO, List<Blog> dtoList, int total){
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

        this.cate = pageRequestDTO.getCate();
    }




}
