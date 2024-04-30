package kr.co.lotte.repository;


import kr.co.lotte.entity.Points;
import kr.co.lotte.entity.Visitor;
import kr.co.lotte.repository.custom.PointsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, String> {


}
