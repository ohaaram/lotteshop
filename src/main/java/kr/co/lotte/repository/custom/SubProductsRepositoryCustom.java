package kr.co.lotte.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotte.entity.SubProducts;

import java.util.List;

public interface SubProductsRepositoryCustom {

    public List<SubProducts> findAllByProdNo(int prodNo);

}
