package kr.co.lotte.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotte.dto.UserPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    public Page<Tuple> selectsUsers(UserPageRequestDTO userPageRequestDTO, Pageable pageable);
    public Tuple selectUser(String uid);


}
