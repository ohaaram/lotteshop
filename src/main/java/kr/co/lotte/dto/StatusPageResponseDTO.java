package kr.co.lotte.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatusPageResponseDTO {


    private List<SellerDTO> dtoList;
    private int rpg;
    private int size;
    private int total;
    private int last;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    @Builder
    public StatusPageResponseDTO(CsFaqPageRequestDTO pageRequestDTO, List<SellerDTO> dtoList , int total){
        this.rpg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((rpg - 1) * size);
        this.end = (int) (Math.ceil(this.rpg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.last = last;
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
