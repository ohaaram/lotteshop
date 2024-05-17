package kr.co.lotte.repository.cs;

import kr.co.lotte.entity.CsQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsQnaRepository extends JpaRepository<CsQna, Integer> {
    List<CsQna> findAll();
    List<CsQna> findAllByWriter(String writer);
}
