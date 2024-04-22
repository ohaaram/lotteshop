package kr.co.lotte.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "seller")
public class Seller {

    @Id
    private String sellerUid;
    private String sellerPass;
    private String sellerName;
    private String sellerCEO;
    private String sellerSaNumber;
    private String sellerTongNumber;
    private String sellerHp;
    private String sellerFax;
    private String sellerEmail;
    private String sellerZip;
    private String sellerAddr1;
    private String sellerAddr2;
    private String sellerDname;
    private String sellerDHp;


}
