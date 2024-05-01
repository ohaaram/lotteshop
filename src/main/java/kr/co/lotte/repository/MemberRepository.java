package kr.co.lotte.repository;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.UserPageRequestDTO;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.custom.MemberRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, String>, MemberRepositoryCustom {

    // 이름과 이메일 찾기 (아이디 찾기용)
    public Optional<User> findUserByNameAndEmail(String name, String email);

    // 아이디와 이메일 찾기 (비밀번호 찾기용)
    public Optional<User> findUserByUidAndEmail(String uid, String email);

    public Page<Tuple> selectsUsers(UserPageRequestDTO userPageRequestDTO, Pageable pageable);
    public Tuple selectUser(String uid);

    List<User> findByRegDateAfter(LocalDate regDate);
}