package kr.co.lotte.repository;

import kr.co.lotte.entity.Categories;
import kr.co.lotte.entity.SubProducts;
import kr.co.lotte.repository.custom.SubProductsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductsRepository extends JpaRepository<SubProducts, Integer> , SubProductsRepositoryCustom {
        public List<SubProducts> findAllByProdNo(int prodNo);

        public List<SubProducts> findAllByProdNoAndColor(int prodNo, String color);
}