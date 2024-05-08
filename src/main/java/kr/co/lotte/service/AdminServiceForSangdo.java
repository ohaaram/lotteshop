package kr.co.lotte.service;


import com.querydsl.core.Tuple;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.dto.UserPageRequestDTO;
import kr.co.lotte.dto.UserPageResponseDTO;
import kr.co.lotte.entity.User;
import kr.co.lotte.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceForSangdo {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public UserPageResponseDTO selectsUserForAdmin(UserPageRequestDTO userPageRequestDTO){

        Pageable pageable = userPageRequestDTO.getPageable();
        Page<Tuple> pageUsers = memberRepository.selectsUsers(userPageRequestDTO, pageable);

        //log.info("selectUsers....1: "+ pageUsers);

        List<UserDTO> dtoList = pageUsers.getContent().stream()
                .map(tuple ->
                        {
                            User user = tuple.get(0, User.class);
                            Integer totalPrice = tuple.get(1, Integer.class);

                            int totalPriceValue = (totalPrice != null) ? totalPrice : 0;
                            user.setTotalPrice(totalPriceValue);

                            return modelMapper.map(user, UserDTO.class);
                        }
                )
                .toList();
        //log.info("selectUsers....2:" + dtoList);
        int total = (int) pageUsers.getTotalElements();

        //log.info("selectUsers....3:" + total);

        return UserPageResponseDTO.builder()
                .userPageRequestDTO(userPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public ResponseEntity<?> updateUserGrade(UserDTO userDTO) {

            log.info("updateGrade...1:"+ userDTO);

            Optional<User> optUser = memberRepository.findById(userDTO.getUid());

            if(optUser.isPresent()){
                log.info("updateGrade.....2");

                User user = optUser.get();

                user.setGrade(userDTO.getGrade());
                log.info("updateGrade....3 :" + user);
                User modifyUser = memberRepository.save(user);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(modifyUser);
            }else {
                log.info("deleteComment.....2");

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("not found");
            }
        }

    public UserDTO selectUserForAdmin(String uid) {
        // uid에 해당하는 사용자의 정보를 페이징해서 조회합니다.
        Tuple tuple = memberRepository.selectUser(uid);
        log.info("selectUser....1: "+ tuple);

        // 튜플을 UserDTO로 변환하는 작업을 수행합니다.
        User user = tuple.get(0, User.class);
        Integer totalPrice = tuple.get(1, Integer.class);
        int totalPriceValue = (totalPrice != null) ? totalPrice : 0;
        user.setTotalPrice(totalPriceValue);

        return modelMapper.map(user, UserDTO.class);
    }

//

}

