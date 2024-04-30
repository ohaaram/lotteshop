package kr.co.lotte.repository;


import kr.co.lotte.entity.Seller;
import kr.co.lotte.entity.Seller_status;
import kr.co.lotte.repository.custom.SellerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Seller_statusRepository extends JpaRepository<Seller_status, Integer>, SellerRepositoryCustom {

}
