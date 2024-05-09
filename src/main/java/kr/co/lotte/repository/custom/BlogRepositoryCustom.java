package kr.co.lotte.repository.custom;

import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.dto.BlogPageRequestDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogRepositoryCustom {

    public List<Blog> selectBlogForStory(String cate);
    public Page<Blog> findAllList(BlogPageRequestDTO pageRequestDTO, Pageable pageable);
}
