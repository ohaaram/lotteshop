package kr.co.lotte.service;

import kr.co.lotte.entity.ProductQna;
import kr.co.lotte.repository.ProductQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductQnaService {

    private final ProductQnaRepository productQnaRepository;

    public List<ProductQna> productQnas(){
        return productQnaRepository.findAll();
    }
}
