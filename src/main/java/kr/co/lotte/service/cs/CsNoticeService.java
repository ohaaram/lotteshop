package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqPageRequestDTO;
import kr.co.lotte.dto.CsFaqPageResponseDTO;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.cs.CsFaqRepository;
import kr.co.lotte.repository.cs.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsNoticeService {

    private final CsNoticeRepository csNoticeRepository;
    @Autowired
    private CsFaqRepository csFaqRepository;

    // notice 작성
    public void insertCsNotice(CsNoticeDTO csNoticeDTO) {
        CsNotice csNotice = csNoticeDTO.toEntity();
        csNoticeRepository.save(csNotice);
    }
    //공지사항 조회
    public CsFaqPageResponseDTO searchNotices(CsFaqPageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable("no");
        Page<CsFaq> lists = csFaqRepository.searchAllCsFaq(requestDTO, pageable);
        List<CsFaq> dtoLists = lists.getContent();
        int total = (int) lists.getTotalElements();
        return new CsFaqPageResponseDTO(requestDTO , dtoLists, total);
    }



}
