package kr.co.lotte.service.cs;

import groovy.util.logging.Log4j2;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsQnaDTO;
import kr.co.lotte.entity.CsQna;
import kr.co.lotte.entity.ProductQna;
import kr.co.lotte.repository.ProductQnaRepository;
import kr.co.lotte.repository.cs.CsFaqRepository;
import kr.co.lotte.repository.cs.CsQnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CsQnaService {

    private final ModelMapper modelMapper;
    private final CsQnaRepository csQnaRepository;
    private final CsFaqRepository csFaqRepository;
    private final ProductQnaRepository productQnaRepository;


    // cs.qna reg
    public void insertQna(CsQnaDTO csQnaDTO){
        // dto를 entity로 변환
        CsQna csQna = modelMapper.map(csQnaDTO, CsQna.class);
        // 매핑된 entity를 db에 저장
        CsQna savedCsQna = csQnaRepository.save(csQna);
    }
    // cs.qna list
    public List<CsQna> qnaList(){
        return csQnaRepository.findAll();
    }

    // cs.qna.list 특정 글 조회
    public CsFaqPageResponseDTO getQnaCate1andCate2(CsFaqPageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable("no");
        Page<CsQna> lists = csFaqRepository.searchAllCsQna(requestDTO, pageable);
        List<CsQna> dtoLists = lists.getContent();
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(dtoLists, requestDTO, total);
    }

    public CsFaqPageResponseDTO getQnaCate1andCate2(CsFaqPageRequestDTO requestDTO, String uid){
        Pageable pageable = requestDTO.getPageable("no");
        Page<CsQna> lists = csFaqRepository.searchAllCsQna(requestDTO, pageable, uid);
        List<CsQna> dtoLists = lists.getContent();
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(dtoLists, requestDTO, total);
    }

    // cs.qna.view 뷰페이지
    public CsQnaDTO qnaView(int no){

        CsQna csQna = csQnaRepository.findById(no).get();
        return modelMapper.map(csQna, CsQnaDTO.class);
    }

    // admin.cs.qna 대답
    public void adminQnaComment(CsQnaDTO csQnaDTO){
        // 번호로 부르기
        int no = csQnaDTO.getNo();

        CsQna csQna = modelMapper.map(csQnaDTO, CsQna.class);
        CsQna savedCsQna = csQnaRepository.save(csQna);

    }

    // prod.view.qna 저장
    public ProductQna writeProdQna(ProductQna productQna){
        productQna.setStatus1("답변 대기중");
        ProductQna savedProductQna = productQnaRepository.save(productQna);
        return modelMapper.map(savedProductQna, ProductQna.class);
    }


    //qna 삭제
    public void delete(int no){

        csQnaRepository.deleteById(no);

    }

    public void delete2(int no){

        productQnaRepository.deleteById(no);

    }
}
