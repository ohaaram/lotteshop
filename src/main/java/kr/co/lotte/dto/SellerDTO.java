package kr.co.lotte.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SellerDTO {

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
