package kr.co.lotte.repository;

import kr.co.lotte.dto.ProdImageDTO;
import kr.co.lotte.entity.ProdImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdImageRepository extends JpaRepository<ProdImage, Integer> {
    public List<ProdImage> findBypNo(int pNo);
}