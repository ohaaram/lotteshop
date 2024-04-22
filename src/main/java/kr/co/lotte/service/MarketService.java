package kr.co.lotte.service;


import com.querydsl.core.Tuple;
import kr.co.lotte.dto.CartsDTO;
import kr.co.lotte.dto.ProdImageDTO;
import kr.co.lotte.dto.ProductsDTO;
import kr.co.lotte.dto.SellerDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.CartsRepository;
import kr.co.lotte.repository.ProductsRepository;
import kr.co.lotte.repository.SellerRepository;
import kr.co.lotte.repository.SubProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarketService {

    private final ProductsRepository marketRepository;
    private final ModelMapper modelMapper;
    private final SubProductsRepository subProductsRepository;
    private final ProductsRepository productRepository;
    private final CartsRepository cartsRepository;

    // 장보기 글보기 페이지 - 장보기 게시글 출력
    public ProductsDTO selectProduct(int prodno){

        return modelMapper.map( productRepository.findById(prodno).get() , ProductsDTO.class);

    }


    public List<SubProducts> findAllByProdNo(int prodno){

        //prodno의 모든 데이터를 출력
        List<SubProducts> Options = subProductsRepository.findAllByProdNo(prodno);

        return Options;
    }

    public ResponseEntity selectOption(String value, int prodNo){
        List<SubProducts> Options2 = subProductsRepository.findAllByProdNoAndColor(prodNo, value);
        Map<String, List<SubProducts>> map = new HashMap<>();
        map.put("sub", Options2);
        log.info(Options2.toString()+"?!!!");
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity inserCart(String userId, List<Integer> counts , List<Integer> subNos){
        int i=0;
        for(int count : counts){
            CartsDTO cartsDTO = new CartsDTO();
            cartsDTO.setCartProdCount(count);
            cartsDTO.setProdNo(subNos.get(i));
            cartsDTO.setUserId(userId);
            cartsRepository.save(modelMapper.map(cartsDTO , Carts.class));
            i++;
        }
        Map<String , String> map = new HashMap<>();
        map.put("result" , "success");
        return ResponseEntity.ok().body(map);
    }

    //장바구니 삭제
    public ResponseEntity deleteCart(List<Integer> lists){
        for(Integer count : lists){
            cartsRepository.deleteById(count);
        }
        Map<String , String> map = new HashMap<>();
        map.put("result" , "success");
        return ResponseEntity.ok().body(map);
    }


    //카트조회
    public List<Carts> selectCart(String userId){
        return cartsRepository.findAllByUserId(userId);
    }
    //카트상품조회
    public List<SubProducts> selectProducts(List<Integer> subProductsNo){
        List<SubProducts> lists = new ArrayList<>();
        for(int a : subProductsNo){
            Tuple sub = productRepository.serachOnlyOne(a);
            SubProducts subProducts = sub.get(1, SubProducts.class);
            subProducts.setProducts(sub.get(0, Products.class));
            lists.add(subProducts);
        }
        return lists;
    }

}
