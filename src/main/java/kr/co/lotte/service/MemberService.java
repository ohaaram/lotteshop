package kr.co.lotte.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.*;

import kr.co.lotte.entity.*;

import kr.co.lotte.mapper.MemberMapper;
import kr.co.lotte.mapper.SellerMapper;
import kr.co.lotte.mapper.TermsMapper;
import kr.co.lotte.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private final SellerMapper sellerMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;


    //회원 등록이 되어 있는지 확인하는 서비스(0또는 1)
    public int selectCountMember(String type, String value) {
        return memberMapper.selectCountMember(type, value);
    }

    public int selectCountSeller(String type, String value) {
        return sellerMapper.selectCountSeller(type, value);
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
        userDTO.setGrade("1");
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

    public ResponseEntity<?> updateUserAddr(UserDTO userDTO) {
        memberMapper.updateUserAddr(userDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("success", "100");
        return ResponseEntity.ok().body(map);
    }

    public void updateUserPassword(UserDTO userDTO) {
        String encoded = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encoded);

        memberMapper.updateUserPassword(userDTO);
    }


    //일반 사용자의 아이디 찾기
    public String findId(UserDTO userDTO, int userType) {

        String uid = null;

        if (userType == 0) {//판매자

            uid = memberMapper.findId2(userDTO);


        } else {//일반 사용자

            uid = memberMapper.findId(userDTO);
        }

        log.info("결과 값으로 찾은 uid : " + uid);

        return uid;
    }


    //일반사용자의 정보 찾기(사용자가 있으면 true, 아니면 false)
    public boolean findPass(String uid, String email) {

        int count1 = memberMapper.findPass1(uid, email);//일반사용자 조회
        int count2 = memberMapper.findPass2(uid, email);//판매자 조회


        log.info("findPass에서 사용자 조회결과 : " + (count1 + count2));

        boolean isPass = false;

        if ((count1 + count2) > 0) {
            isPass = true;
        }
        return isPass;
    }

    // 랜덤한 비밀번호 생성
    private static String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }


    //사용자에게 임시 비밀번호를 이메일로 전송
    public void tempPass(String receiver, String uid, boolean isSeller) {

        int length = 8; // 비밀번호 길이

        //MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        String password = generateRandomPassword(length);
        log.info("임시 비밀번호(랜덤값으로 만든것): " + password);

        //랜덤값을 인코더하여 해당 아이디의 비밀번호로 저장
        String encodePass = passwordEncoder.encode(password);

        if (isSeller) {//판매자

            SellerDTO sellerDTO = new SellerDTO();

            sellerDTO.setSellerUid(uid);

            sellerDTO.setSellerPass(encodePass);

            memberMapper.updateSellerPassword(sellerDTO);

        } else {//일반 사용자

            UserDTO userDTO = new UserDTO();

            userDTO.setPass(encodePass);

            userDTO.setUid(uid);

            //임시 비밀번호로 저장
            memberMapper.updateUserPassword(userDTO);
        }

        String title = "lotteShop 임시비밀번호 입니다.";
        String content = "<h1>임시비밀번호는 " + password + "입니다.<h1>";

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


    //세개의 값을 이용해서 사용자 탐색
    public int findMember(String email, String name, String hp) {

        log.info("memberService - findMember - email : "+ email);
        log.info("memberService - findMember - name : "+ name);
        log.info("memberService - findMember - hp : "+ hp);

        int count1 = memberMapper.findMember1(email, name, hp);//일반유저에서 검색
        int count2 = memberMapper.findMember2(email, name, hp);//판매자에서 검색

        log.info("count1 : "+count1);
        log.info("count2 : "+count2);

        int count = count1 + count2;//검색 결과가 하나라도 있으면 가입이 되어있는 것

        log.info("총 count : "+count);

        return count;
    }


    public void sendEmailForSeller(String receiver) {
        log.info("sender={}", sender);

        //MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        String title = "lotteON 승인";
        String content = "축하드립니다!!! 판매자 권한이 승인되었습니다!";

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

    public int findUserForDel(String uid){

        UserDTO userDTO = memberMapper.findUser(uid);

        userDTO.setRole("None");
        userDTO.setStatus(0);

        User user = modelMapper.map(userDTO, User.class);

        if(user!=null) {

            memberRepository.save(user);

            return 1;//값이 바뀌면 1

        }else{

            return 0;//아니면 0
        }
    }

}



