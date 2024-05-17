package kr.co.lotte.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SellerRepositoryCustom  {


    public Page<Tuple> seller_status(CsFaqPageRequestDTO pageRequestDTO, Pageable pageable);

    public List<Seller> waitingSellers();

}
