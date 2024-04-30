package kr.co.lotte.repository;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.UserPageRequestDTO;
import kr.co.lotte.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

 //   public Page<Tuple> selectsUsers(UserPageRequestDTO userPageRequestDTO, Pageable pageable);
}