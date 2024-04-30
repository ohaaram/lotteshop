package kr.co.lotte.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrdersPageRequestDTO {

    @Builder.Default
    private  int no =1;

    @Builder.Default
    private  int pg=1;

    @Builder.Default
    private  int size =10;

    private String currentMonth;
    private String oneWeek;
    private String fifteen;
    private String dateBegin;
    private String dateEnd;
    
    @Builder.Default
    private String state ="주문 대기";

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }
}