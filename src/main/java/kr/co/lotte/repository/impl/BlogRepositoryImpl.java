package kr.co.lotte.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.QBlog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.BooleanExpression;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BlogRepositoryImpl {

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
}
