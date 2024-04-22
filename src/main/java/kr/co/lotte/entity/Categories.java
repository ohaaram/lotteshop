package kr.co.lotte.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name="categories")

public class Categories {
    @Id
    private int cateNo;
    private  String cateName;
    private  String secondCateName;
    private  String thirdCateName;
}
