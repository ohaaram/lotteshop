package kr.co.lotte.mapper;

import kr.co.lotte.dto.TermsDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TermsMapper {
    public void modifyTerms(TermsDTO termsDTO);
    public TermsDTO findTerms(int pk);
}
