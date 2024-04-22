package kr.co.lotte.repository;


import kr.co.lotte.entity.Products;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Integer>, ProductsRepositoryCustom {
}
