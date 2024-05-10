package kr.co.lotte.dto;

import kr.co.lotte.entity.Points;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PointsPageResponseDTO {

    private List<Points> dtoList;
    private int currentMonth;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private String oneWeek;
    private String fifteen;

    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;



    @Builder
    public PointsPageResponseDTO(PointsPageRequestDTO pageRequestDTO, List<Points> dtoList, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.fifteen=pageRequestDTO.getFifteen();
        this.oneWeek=pageRequestDTO.getOneWeek();
        this.currentMonth = Integer.parseInt(pageRequestDTO.getCurrentMonth());
        this.dateBegin = pageRequestDTO.getDateBegin();
        this.dateEnd = pageRequestDTO.getDateEnd();

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }



}