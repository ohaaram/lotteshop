package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.mapper.AdminCsMapper;
import kr.co.lotte.repository.cs.CsFaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CsFaqService {

    private final AdminCsMapper adminCsMapper;
    private final CsFaqRepository csFaqRepository;
    private final ModelMapper modelMapper;

    // admin.cs.faq.list 출력
    public Page<CsFaq> getAdminFaqArticles(Pageable pageable) {
        return csFaqRepository.findAll(pageable);
    }

    // cs.faq 출력
    public List<List<CsFaqDTO>> getFaqArticles(String cate1) {

        List<CsFaq> list = csFaqRepository.findByCate1(cate1);
        List<CsFaqDTO> dtoList = list.stream().map((entity)-> modelMapper.map(entity, CsFaqDTO.class)).toList();

        Map<String, List<CsFaqDTO>> groupedByCate2 = dtoList.stream().collect(Collectors.groupingBy(CsFaqDTO::getCate2));



        log.info("dtoList cate2 : " + groupedByCate2);

        List<List<CsFaqDTO>> dtoLists = groupedByCate2.values()
                .stream()
                .collect(Collectors.toList());
        //List<CsFaqDTO> dtoList = list.stream().map((entity)-> modelMapper.map(entity, CsFaqDTO.class)).toList();
        return dtoLists;
    }

    // admin.cs.faq 이거저거 출력
    public CsFaqDTO faqSelectNo(int no){
        return adminCsMapper.faqSelectNo(no);
    }

    // admin.cs.faq 수정
    public void adminFaqUpdate(CsFaqDTO csFaqDTO){
        adminCsMapper.adminFaqUpdate(csFaqDTO);
    }

    // admin.cs.faq 삭제
    public void adminFaqDelete(int no){
        adminCsMapper.adminFaqDelete(no);
    }

    // admin.cs.faq 선택삭제
    public void adminFaqDeleteSelected(List<Integer> selectedNo){

        adminCsMapper.deleteFaqByIds(selectedNo);

    }

    // admin.cs.faq 글 작성
    public void adminFaqWrite(CsFaqDTO csFaqDTO){
        CsFaq csFaq =csFaqRepository.findFirstByCate2(csFaqDTO.getCate2());
        csFaqDTO.setCatename(csFaq.getCatename());
        csFaqRepository.save(modelMapper.map(csFaqDTO, CsFaq.class));
    }

    // admin.cs.list 특정 글 조회
    public CsFaqPageResponseDTO getFaqsCate1and2(CsFaqPageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable("no");
        Page<CsFaq> lists = csFaqRepository.searchAllCsFaq(requestDTO, pageable);
        List<CsFaq> dtoLists = lists.getContent();
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(requestDTO , dtoLists, total);
    }

}
