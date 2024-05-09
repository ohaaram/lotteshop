package kr.co.lotte.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="blogimg")
public class BlogImg {
    @Id
    private int bwno;
    private int bno;
    private String oName;
    private String sName;

}
