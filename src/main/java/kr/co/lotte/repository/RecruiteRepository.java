package kr.co.lotte.repository;


import kr.co.lotte.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiteRepository extends JpaRepository<Recruit,Integer> {
}
