package kr.co.lotte.repository.custom;

import kr.co.lotte.dto.BlogDTO;

import java.util.List;

public interface BlogRepositoryCustom {

    public List<BlogDTO> selectBlogForStory(String cate);



}
