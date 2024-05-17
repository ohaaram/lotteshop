package kr.co.lotte.repository.custom;


import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;

public interface CsRepositoryCustom {
    public Page<CsFaq> searchAllCsFaq(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable );
    public Page<CsNotice> searchAllCsNotice(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<CsQna> searchAllCsQna(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<CsQna> searchAllCsQna(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable, String uid);
    public Page<ProductQna> searchAllProdQna(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<ProductQna> searchAllProdQna(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable, String uid);
    public Page<ProductQna> searchAllProdQnaSeller(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable, String uid);
}
