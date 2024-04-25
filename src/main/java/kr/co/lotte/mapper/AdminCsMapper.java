package kr.co.lotte.mapper;

import kr.co.lotte.dto.CsFaqDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCsMapper {

    // admin.cs.faq.modify 출력
    public CsFaqDTO faqSelectNo(int no);
    // admin.cs.faq 수정
    public void adminFaqUpdate(CsFaqDTO csFaqDTO);
    // admin.cs.faq 삭제
    public void adminFaqDelete(int no);
    // admin.cs.faq 선택삭제
    public void deleteFaqByIds(List<Integer> selectedNo);
    // admin.cs.faq 글 작성
    public void adminFaqWrite(CsFaqDTO csFaqDTO);

}
