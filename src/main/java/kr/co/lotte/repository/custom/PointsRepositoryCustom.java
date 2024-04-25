package kr.co.lotte.repository.custom;


import com.querydsl.core.Tuple;
import kr.co.lotte.dto.PointsPageRequestDTO;
import kr.co.lotte.entity.Points;
import kr.co.lotte.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointsRepositoryCustom {
    public Page<Points> searchAllPointsForList(PointsPageRequestDTO requestDTO , Pageable pageable , String uid);
}
