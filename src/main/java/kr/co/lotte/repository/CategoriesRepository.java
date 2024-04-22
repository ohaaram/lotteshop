package kr.co.lotte.repository;

import kr.co.lotte.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    public List<Categories> findAllByCateName(String catename);
    public List<Categories> findAllBySecondCateName(String catename);

}