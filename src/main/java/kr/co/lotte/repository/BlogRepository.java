package kr.co.lotte.repository;

import kr.co.lotte.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    public List<Blog> findTop5ByOrderByDateDesc();

    public List<Blog> selectBlogForStory(String cate);


}
