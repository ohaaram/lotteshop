package kr.co.lotte.dto;

import kr.co.lotte.entity.OrderItems;
import kr.co.lotte.entity.Orders;
import kr.co.lotte.entity.Points;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersPageResponseDTO {

    private List<Orders> dtoList;
    private List<OrderItems> dtoList2;
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

    private String state;

    @Builder
    public OrdersPageResponseDTO(OrdersPageRequestDTO pageRequestDTO, List<Orders> dtoList, int total){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
        this.fifteen=pageRequestDTO.getFifteen();
        this.oneWeek=pageRequestDTO.getOneWeek();
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

    @Builder
    public OrdersPageResponseDTO(OrdersPageRequestDTO pageRequestDTO,int total, List<OrderItems> dtoList2){
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList2 = dtoList2;
        this.state = pageRequestDTO.getState();
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