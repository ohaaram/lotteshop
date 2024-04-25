package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.MainProductsPageResponseDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsPageResponseDTO;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import kr.co.lotte.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {
    @Autowired
    private ProductsRepository productsRepository;

    //히트상품 (많이 판매된 순)
    public List<Products> selectHitProducts(){
        return productsRepository.findFirst8ByOrderByProdSoldDesc();
    }

    //추천상품 (할인율 높은순)
    public List<Products> selectRecomendProducts(){
        return productsRepository.findFirst8ByOrderByProdDiscountDesc();
    }
    //최신상품 (최신순)
    public List<Products> selectRecentProducts(){
        return productsRepository.findFirst8ByOrderByProdNoDesc();
    }


    //할인상품  (할인된 것에서 최신순)
    public List<Products> selectDiscountProducts(){
        return productsRepository.findFirst8ByDiscount();
    }

    //상품어쩌고정렬
    public MainProductsPageResponseDTO searchListProducts(MainProductsPageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Products> page = productsRepository.searchAllProductsForList(requestDTO, pageable);
        List<Products> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new MainProductsPageResponseDTO(requestDTO, dtoList , total);

    }
}
