package kr.co.lotte.dto;

import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.entity.CsNotice;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CsFaqPageResponseDTO {

    private List<CsFaq> dtoList;
    private List<CsNotice> dtoList2;
    private String currentMonth;
    private String oneWeek;
    private String fifteen;
    private String dateBegin;
    private String dateEnd;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;
    private String cate1;
    private String cate2;
    @Builder
    public CsFaqPageResponseDTO(CsFaqPageRequestDTO pageRequestDTO, List<CsFaq> dtoList, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.cate1 = pageRequestDTO.getCate1();
        this.cate2 = pageRequestDTO.getCate2();
        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

    @Builder
    public CsFaqPageResponseDTO(CsFaqPageRequestDTO pageRequestDTO, int total , List<CsNotice> dtoList2){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList2 = dtoList2;
        this.cate1 = pageRequestDTO.getCate1();
        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }



}