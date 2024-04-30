package kr.co.lotte.repository.custom;


import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;

public interface CsRepositoryCustom {
    public Page<CsFaq> searchAllCsFaq(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable );
    public Page<CsNotice> searchAllCsNotice(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable);
}
