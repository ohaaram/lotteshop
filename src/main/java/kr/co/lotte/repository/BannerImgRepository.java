package kr.co.lotte.repository;

import kr.co.lotte.entity.BannerImg;
import kr.co.lotte.repository.custom.BannerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerImgRepository extends JpaRepository<BannerImg,Integer>{

    public void deleteBybno(int bno);

}
