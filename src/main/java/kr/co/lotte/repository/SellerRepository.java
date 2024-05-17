package kr.co.lotte.repository;

import com.querydsl.core.Tuple;
import kr.co.lotte.entity.Categories;
import kr.co.lotte.entity.Seller;
import kr.co.lotte.repository.custom.SellerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> , SellerRepositoryCustom {

}