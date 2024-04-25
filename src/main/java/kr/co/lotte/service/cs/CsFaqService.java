package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.dto.CsFaqDTO;
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

        Map<String, List<CsFaqDTO>> groupedByCate2 = dtoList.stream()
                .collect(Collectors.groupingBy(CsFaqDTO::getCate2));

        log.info("dtoList cate2 : " + groupedByCate2);

        List<List<CsFaqDTO>> dtoLists = groupedByCate2.values()
                .stream()
                .collect(Collectors.toList());

        //List<CsFaqDTO> dtoList = list.stream().map((entity)-> modelMapper.map(entity, CsFaqDTO.class)).toList();

        return dtoLists;
    }

    // cs.faq 더보기
    public List<List<CsFaqDTO>> getAdditionalFaqArticles(String cate1) {
        // 추가 데이터를 가져오는 로직을 작성합니다.
        // 예를 들어, 이 메서드에서는 기존에 가져온 FAQ 항목들 이후의 항목들을 가져오는 로직을 작성해야 합니다.
        // 이는 데이터베이스에서 다음 페이지의 항목들을 가져오는 등의 방식으로 구현할 수 있습니다.

        // 기존 데이터 이후의 항목들을 가져오는 로직을 수행합니다.
        List<CsFaq> additionalFaqList = csFaqRepository.findByCate1(cate1);

        // 가져온 추가 항목들을 CsFaqDTO로 변환합니다.
        List<CsFaqDTO> additionalDtoList = additionalFaqList.stream()
                .map(entity -> modelMapper.map(entity, CsFaqDTO.class))
                .collect(Collectors.toList());

        // 추가 데이터를 그룹화하여 반환합니다. 이는 클라이언트 측에서 추가 데이터를 화면에 표시하는 데 사용됩니다.
        Map<String, List<CsFaqDTO>> additionalGroupedByCate2 = additionalDtoList.stream()
                .collect(Collectors.groupingBy(CsFaqDTO::getCate2));

        // 그룹화된 추가 데이터를 리스트로 변환하여 반환합니다.
        List<List<CsFaqDTO>> additionalDtoLists = additionalGroupedByCate2.values()
                .stream()
                .collect(Collectors.toList());

        return additionalDtoLists;
    }

    // admin.cs.faq.modify 출력
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
        adminCsMapper.adminFaqWrite(csFaqDTO);
    }

    //
    public List<String> getCate2ByCate1(String cate1){
        //return csFaqRepository.findCate2ByCate1(cate1);
        return null;
    }

    /*
    // faq.user 출력
    public List<CsFaq> getLotteonersArticles(){
        return csFaqRepository.findByCate("lotteOners");
    }
    public List<CsFaq> getRegArticles(){
        return csFaqRepository.findByCate("reg");
    }
    public List<CsFaq> getInfoArticles(){
        return csFaqRepository.findByCate("info");
    }
    public List<CsFaq> getGradeArticles(){
        return csFaqRepository.findByCate("grade");
    }
    public List<CsFaq> getDelArticles(){
        return csFaqRepository.findByCate("del");
    }

    // faq.eventCupon 출력
    public List<CsFaq> getLpointArticles(){
        return csFaqRepository.findByCate("lpoint");
    }
    public List<CsFaq> getLstampArticles(){
        return csFaqRepository.findByCate("lstamp");
    }
    public List<CsFaq> getReviewArticles(){
        return csFaqRepository.findByCate("review");
    }
    public List<CsFaq> getOnmileArticles(){
        return csFaqRepository.findByCate("onmile");
    }
    public List<CsFaq> getEventArticles(){
        return csFaqRepository.findByCate("event");
    }

    // faq.ord 출력
    public List<CsFaq> getLpayArticle(){
        return csFaqRepository.findByCate("lpay");
    }
    public List<CsFaq> getEtcArticle(){
        return csFaqRepository.findByCate("etc");
    }
    public List<CsFaq> getMutongArticle(){
        return csFaqRepository.findByCate("mutong");
    }
    public List<CsFaq> getOrdArticle(){
        return csFaqRepository.findByCate("ord");
    }
    public List<CsFaq> getOrdlistArticle(){
        return csFaqRepository.findByCate("ordlist");
    }
    public List<CsFaq> getCardArticle(){
        return csFaqRepository.findByCate("card");
    }

    // faq.delivery 출력
    public List<CsFaq> getBuyArticle(){
        return csFaqRepository.findByCate("buy");
    }
    public List<CsFaq> getDelpArticle(){
        return csFaqRepository.findByCate("delp");
    }
    public List<CsFaq> getDelmArticle(){
        return csFaqRepository.findByCate("delm");
    }
    public List<CsFaq> getDelinfoArticle(){
        return csFaqRepository.findByCate("delinfo");
    }
    public List<CsFaq> getGiftArticle(){
        return csFaqRepository.findByCate("gift");
    }

    // faq.cancel 출력
    public List<CsFaq> getOrdCancelArticle(){
        return csFaqRepository.findByCate("ordcancel");
    }
    public List<CsFaq> getRefundArticle(){
        return csFaqRepository.findByCate("refund");
    }
    public List<CsFaq> getAsArticle(){
        return csFaqRepository.findByCate("as");
    }
    public List<CsFaq> getAspArticle(){
        return csFaqRepository.findByCate("asp");
    }
    public List<CsFaq> getChangeArticle(){
        return csFaqRepository.findByCate("change");
    }
    public List<CsFaq> getReturnsArticle(){
        return csFaqRepository.findByCate("returns");
    }

    // faq.cancel 출력
    public List<CsFaq> getEtcOrdArticle(){
        return csFaqRepository.findByCate("etcord");
    }
    public List<CsFaq> getEtcCardArticle(){
        return csFaqRepository.findByCate("etccard");
    }
    public List<CsFaq> getCashReceiptArticle(){
        return csFaqRepository.findByCate("cashreceipt");
    }
    public List<CsFaq> getTaxReceiptArticle(){
        return csFaqRepository.findByCate("taxreceipt");
    }

     */

}
