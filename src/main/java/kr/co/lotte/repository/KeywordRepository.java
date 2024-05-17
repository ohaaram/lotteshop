package kr.co.lotte.repository;


import kr.co.lotte.entity.Search;
import kr.co.lotte.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Search, String> {
    public List<Search> findFirst10ByOrderByCountDesc();

}
