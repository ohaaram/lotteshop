package kr.co.lotte.dto;

import jakarta.persistence.Id;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LikeDTO {

        private String userId;
        private int prodNo;

}
