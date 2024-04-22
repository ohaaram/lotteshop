package kr.co.lotte.service.cs;

import groovy.util.logging.Slf4j;
import kr.co.lotte.entity.CsFaq;
import kr.co.lotte.repository.CsFaqRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsFaqService {

    private static final Logger log = LoggerFactory.getLogger(CsFaqService.class);
    private final CsFaqRepository csFaqRepository;

    // admin.cs.faq.list 출력
    public Page<CsFaq> getFaqArticles(Pageable pageable) {
        return csFaqRepository.findAll(pageable);
    }


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

}
