package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.MainProductsPageResponseDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.dto.ProductsPageResponseDTO;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.SubProducts;
import kr.co.lotte.entity.Visitor;
import kr.co.lotte.repository.LikeRepository;
import kr.co.lotte.repository.OrdersRepository;
import kr.co.lotte.repository.ProductsRepository;
import kr.co.lotte.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MainService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private LikeRepository likeRepository;

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

    //방문자수
    public void upDateVisitor(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        if(visitorRepository.findById(formattedDate).isPresent()){
            Visitor visitor =visitorRepository.findById(formattedDate).get();
            visitor.setVisitCount(visitor.getVisitCount()+1);
            visitorRepository.save(visitor);
        }else{
            Visitor visitor = new Visitor();
            visitor.setVisitorDate(formattedDate);
            visitor.setVisitCount(1);
            visitorRepository.save(visitor);
        }
    }


    public List<Products> hahaha (List<Products> products, String uid){
        for (Products product : products){
            if(likeRepository.findByUserIdAndProdNo(uid, product.getProdNo()).isEmpty()){
                product.setLikeState(0);
            }else{
                product.setLikeState(1);
            }
        }
        return products;
    }

    //상품 검색
    public ProductsPageResponseDTO searchForProduct(ProductsPageRequestDTO requestDTO, String keyword){
        Pageable pageable = requestDTO.getPageable("no");

        //키워드를 받아와서 검색 후  페이지 네이션
        Page<Products> page = productsRepository.searchForProduct(requestDTO,pageable,keyword);
        List<Products>  dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new ProductsPageResponseDTO(requestDTO,dtoList,total);
    }

}
