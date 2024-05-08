package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.cs.CsFaqRepository;
import kr.co.lotte.repository.cs.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsNoticeService {

    private static final Logger log = LoggerFactory.getLogger(CsNoticeService.class);
    private final CsNoticeRepository csNoticeRepository;
    @Autowired
    private CsFaqRepository csFaqRepository;
    private final ModelMapper modelMapper;

    // admin.cs.notice 작성
    public void insertCsNotice(CsNoticeDTO csNoticeDTO) {
        CsNotice csNotice = modelMapper.map(csNoticeDTO, CsNotice.class);
        log.info("Insert CsNotice : {}", csNotice.toString());
        CsNotice savedNotice = csNoticeRepository.save(csNotice);
        log.info("Insert CsNotice : {}", savedNotice.toString());
    }

    // admin.cs.notice 번호로 글 조회
    public CsNoticeDTO adminNoticeView(int no){

        CsNotice csNotice = csNoticeRepository.findById(no).get();
        return modelMapper.map(csNotice, CsNoticeDTO.class);
    }

    // admin.cs.notice 수정
    public void adminNoticeUpdate(CsNoticeDTO csNoticeDTO) {

        // 글 번호 불러오기
        int no = csNoticeDTO.getNo();
        CsNotice csNotice = modelMapper.map(csNoticeDTO, CsNotice.class);
        CsNotice savedNotice = csNoticeRepository.save(csNotice);

    }

    // admin.cs.notice 삭제
    public void adminNoticeDelete(int csNoticeNo) {
        csNoticeRepository.deleteById(csNoticeNo);
    }

    // cs.notice list
    public List<CsNotice> noticeList(){
        return csNoticeRepository.findAll();
    }
    // admin.cs.list 특정 글 조회
    public CsFaqPageResponseDTO getNoticeCate1andCate2(CsFaqPageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable("no");
        Page<CsNotice> lists = csFaqRepository.searchAllCsNotice(requestDTO, pageable);
        List<CsNotice> dtoLists = lists.getContent();
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(requestDTO , total, dtoLists);
    }


}
