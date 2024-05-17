package kr.co.lotte.service;

import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsQnaDTO;
import kr.co.lotte.dto.ProductQnaDTO;
import kr.co.lotte.entity.CsQna;
import kr.co.lotte.entity.ProductQna;
import kr.co.lotte.repository.ProductQnaRepository;
import kr.co.lotte.repository.ProductsRepository;
import kr.co.lotte.repository.cs.CsFaqRepository;
import kr.co.lotte.repository.cs.CsQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductQnaService {

    private final ProductQnaRepository productQnaRepository;
    private final CsFaqRepository csFaqRepository;
    private final ModelMapper modelMapper;
    private final ProductsRepository productsRepository;
    private final CsQnaRepository csQnaRepository;


    public CsFaqPageResponseDTO productQnas( CsFaqPageRequestDTO csFaqPageRequestDTO, int prodNo){

        Pageable pageable = csFaqPageRequestDTO.getPageable2("no");
        Page<ProductQna> productQnas = productQnaRepository.findByProdNo(prodNo, pageable);
        log.info(prodNo+"here~");
        log.info(productQnas+"here~");
        List<ProductQna>   productQnasList = productQnas.getContent();
        int total = (int) productQnas.getTotalElements();
        return new CsFaqPageResponseDTO(total,  productQnasList, csFaqPageRequestDTO );

    }

    public List<ProductQna> productQnas( ){
        return  productQnaRepository.findAll();

    }

    // cs.qna.reply2 뷰페이지
    public ProductQnaDTO prodQnaView(int no){

        ProductQna productQna = productQnaRepository.findById(no).get();
        return modelMapper.map(productQna, ProductQnaDTO.class);
    }

    // cs.prodQna 답변
    public void adminProdQnaAnswer(ProductQnaDTO productQnaDTO){
        // 번호로 부르기
        int no = productQnaDTO.getNo();

        ProductQna productQna = modelMapper.map(productQnaDTO, ProductQna.class);
        ProductQna savedProdQna = productQnaRepository.save(productQna);

    }

    public List<ProductQna> prodSellerQna(String sellerUid){

        return productQnaRepository.findBySellerUid(sellerUid);
    };

    public CsFaqPageResponseDTO getProdQnaCate(CsFaqPageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable("no");
        Page<ProductQna> lists = csFaqRepository.searchAllProdQna(requestDTO, pageable);
        List<ProductQna> dtoLists = lists.getContent();
        for(ProductQna productQna : dtoLists){
            productQna.setProductName(productsRepository.findById(productQna.getProdNo()).get().getProdName());
        }
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(dtoLists, total, requestDTO);
    }

    public CsFaqPageResponseDTO getProdQnaCate(CsFaqPageRequestDTO requestDTO, String uid){
        Pageable pageable = requestDTO.getPageable("no");
        Page<ProductQna> lists = csFaqRepository.searchAllProdQnaSeller(requestDTO, pageable, uid);
        List<ProductQna> dtoLists = lists.getContent();
        for (ProductQna productQna : dtoLists) {
            productQna.setProductName(productsRepository.findById(productQna.getProdNo()).get().getProdName());
        }
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(dtoLists, total, requestDTO);
    }

    public ResponseEntity getProdQna(int prodNo){
        Map<String , ProductQna> map = new HashMap<>();
        map.put("data", productQnaRepository.findById(prodNo).get());
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity getProdQna2(int prodNo){
        Map<String , CsQna> map = new HashMap<>();
        map.put("data", csQnaRepository.findById(prodNo).get());
        return ResponseEntity.ok().body(map);
    }
}
