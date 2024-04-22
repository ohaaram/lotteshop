package kr.co.lotte.repository;

import kr.co.lotte.entity.Carts;
import kr.co.lotte.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Integer> {

    public List<Carts> findAllByUserId(String userId);

}