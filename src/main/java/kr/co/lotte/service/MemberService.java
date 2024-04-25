package kr.co.lotte.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.SellerDTO;
import kr.co.lotte.dto.TermsDTO;
import kr.co.lotte.dto.UserDTO;

import kr.co.lotte.dto.UserUpdateDTO;
import kr.co.lotte.entity.Points;

import kr.co.lotte.entity.Seller;
import kr.co.lotte.entity.User;
import kr.co.lotte.mapper.MemberMapper;
import kr.co.lotte.mapper.TermsMapper;
import kr.co.lotte.repository.MemberRepository;
import kr.co.lotte.repository.PointsRepository;
import kr.co.lotte.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final MemberMapper memberMapper;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final TermsMapper termsMapper;
    private final PointsRepository pointsRepository;


    //회원 등록이 되어 있는지 확인하는 서비스(0또는 1)
    public int selectCountMember(String type, String value) {
        return memberMapper.selectCountMember(type, value);
    }

    //이메일 보내기 서비스
    @Value("${spring.mail.username}")//이메일 보내는 사람 주소
    private String sender;

    public void sendEmailCode(HttpSession session, String receiver) {
        log.info("sender={}", sender);

        //MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        //인증코드 생성 후 세션 저장
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        session.setAttribute("code", code);

        log.info("code={}", code);

        String title = "lotteShop 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.<h1>";

        try {
            message.setSubject(title);
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("error={}", e.getMessage());
        }

    }

    // 회원 가입 서비스
    public void insert(UserDTO userDTO) {
        String encodedPass = passwordEncoder.encode(userDTO.getPass());

        userDTO.setPass(encodedPass);

        userDTO.setRole("USER");
        userDTO.setTotalPoint(5000);
        User user = modelMapper.map(userDTO, User.class);
        memberRepository.save(user);
        Points points = new Points();
        points.setUserId(userDTO.getUid());
        points.setPoint(5000);
        points.setPointDesc("회원가입 적립");
        points.setState("적립");

        LocalDateTime currentDate = LocalDateTime.now();
        // 날짜 증가
        LocalDateTime modifiedDate = currentDate.plusYears(1);
        points.setEndDateTime(modifiedDate);
        pointsRepository.save(points);
    }

    public void insert(SellerDTO sellerDTO) {
        String encodedPass = passwordEncoder.encode(sellerDTO.getSellerPass());

        sellerDTO.setSellerPass(encodedPass);

        Seller seller = modelMapper.map(sellerDTO, Seller.class);
        sellerRepository.save(seller);
    }

    public UserDTO login(UserDTO userDTO) {
        Optional<User> findUser = memberRepository.findById(userDTO.getUid());
        if (findUser.isPresent()) {

            User user = findUser.get();

            if (user.getPass().equals(userDTO.getPass())) {
                return modelMapper.map(user, UserDTO.class);
            }
            return null;
        }
        return null;
    }

    public UserDTO findUser(String uid) {
        return memberMapper.findUser(uid);
    }

    //terms
    public TermsDTO findTerms(int intPk) {

        return termsMapper.findTerms(intPk);
    }

    public ResponseEntity<?> myInfoUpdate(String type, String value, String uid) {
        memberMapper.updateUserForType(type, value, uid);
        Map<String, Object> map = new HashMap<>();
        map.put("success", "100");
        return ResponseEntity.ok().body(map);
    }

}



