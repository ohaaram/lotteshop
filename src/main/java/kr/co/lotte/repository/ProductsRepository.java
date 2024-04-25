package kr.co.lotte.repository;


import kr.co.lotte.entity.Products;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Integer>, ProductsRepositoryCustom {

    public List<Products> findFirst8ByOrderByProdSoldDesc();

    public List<Products> findFirst8ByOrderByProdDiscountDesc();

    public List<Products> findFirst8ByOrderByProdNoDesc();

    @Query("select p from Products p where p.prodDiscount > 0 order by p.RegProdDate desc limit 8")
    public List<Products> findFirst8ByDiscount();

}
