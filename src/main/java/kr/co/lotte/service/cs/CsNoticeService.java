package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsNoticeDTO;
import kr.co.lotte.entity.CsNotice;
import kr.co.lotte.repository.CsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsNoticeService {

    private final CsNoticeRepository csNoticeRepository;

    // notice 작성
    public void insertCsNotice(CsNoticeDTO csNoticeDTO) {
        CsNotice csNotice = csNoticeDTO.toEntity();
        csNoticeRepository.save(csNotice);
    }




}
