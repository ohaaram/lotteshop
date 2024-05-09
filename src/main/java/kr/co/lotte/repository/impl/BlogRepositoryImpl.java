package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.dto.BlogPageRequestDTO;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.Like;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.QBlog;
import kr.co.lotte.repository.custom.BlogRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBlog qblog = QBlog.blog;

    public List<Blog> selectBlogForStory(String cate){

        //블로그 테이블에서 카테고리가 같은것만 출력
        List<Blog> blogList_cate=jpaQueryFactory.select(qblog)
                .from(qblog)
                .where(qblog.cate.eq(cate))
                .fetch();

        log.info("BlogRepositoryImpl - blogList_cate={}",blogList_cate);

        return blogList_cate;

    }

    @Override
    public Page<Blog> findAllList(BlogPageRequestDTO pageRequestDTO, Pageable pageable) {
        String cate = pageRequestDTO.getCate();

        QueryResults<Blog> results = null;

        log.info("여기는 Blog임플 - findAllList - cate : "+cate);

        if(cate!=null&&!cate.isEmpty()) {

            results = jpaQueryFactory
                    .select(qblog)
                    .from(qblog)
                    .where(qblog.cate.eq(cate))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else{
            results = jpaQueryFactory
                    .select(qblog)
                    .from(qblog)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<Blog> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
