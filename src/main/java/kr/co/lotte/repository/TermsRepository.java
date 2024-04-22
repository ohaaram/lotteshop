package kr.co.lotte.repository;

import kr.co.lotte.entity.ProdImage;
import kr.co.lotte.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Integer> {
}