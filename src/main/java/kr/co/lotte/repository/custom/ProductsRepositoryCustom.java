package kr.co.lotte.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsPageResponseDTO;
import kr.co.lotte.entity.Like;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepositoryCustom  {

    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> searchAllProductsForAdmin(ProductsPageRequestDTO pageRequestDTO, Pageable pageable, String uid);
    // market/view 페이지 product 조회
    public List<Tuple> selectProduct(int prodno);

    public Tuple serachOnlyOne(int subProductNo);

    public Page<Tuple> searchAllProductsForList(MainProductsPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Products> searchAllProductsForSeller(MainProductsPageRequestDTO pageRequestDTO, Pageable pageable);


    public Page<Like> searchAllLike(ProductsPageRequestDTO pageRequestDTO, Pageable pageable, String uid);

    public Page<Tuple> searchForProduct(ProductsPageRequestDTO productsPageRequestDTO, Pageable pageable);

}
