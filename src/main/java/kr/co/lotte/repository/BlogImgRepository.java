package kr.co.lotte.repository;

import kr.co.lotte.entity.BlogImg;
import kr.co.lotte.repository.custom.BlogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogImgRepository extends JpaRepository<BlogImg,Integer> {

    public void deleteByBno(int bno);

    public BlogImg findByBno(int bno);
}
