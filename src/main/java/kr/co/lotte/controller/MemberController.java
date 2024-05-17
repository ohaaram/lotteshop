package kr.co.lotte.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotte.dto.BannerDTO;
import kr.co.lotte.dto.SellerDTO;
import kr.co.lotte.dto.TermsDTO;
import kr.co.lotte.dto.UserDTO;
import kr.co.lotte.entity.Policy;
import kr.co.lotte.service.AdminService;
import kr.co.lotte.service.MemberService;
import kr.co.lotte.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final PolicyService policyService;

    @GetMapping("/member/join")
    public String join() {
        return "/member/join";
    }

    @GetMapping("/member/login")

    public String login(Model model , HttpServletRequest request, @RequestParam(name = "success", required = false)String success) {

        String previousUrl = request.getHeader("Referer");
        HttpSession session = request.getSession();
        if(session.getAttribute("previousUrl") == null && success == null) {
            session.setAttribute("previousUrl", previousUrl);
        }

        return "/member/login";
    }

//    @PostMapping("/member/login")
//    public String login(UserDTO userDTO, Model model) {
//
//        memberService.login(userDTO);
//        return "/index";
//    }

    //회원가입 페이지 매핑
    @GetMapping("/member/register")
    public String register(HttpSession session) {
        if(session.getAttribute("terms") == null){
            return "redirect:/member/signup";
        }
        return "/member/register";
    }

    //타입에 따라 db에 있는지 중복확인을 시켜줌. if (type == email) {중복검사 후 메일 전송}
    @ResponseBody
    @GetMapping("/member/check/{type}/{value}")
    public ResponseEntity<?> checkUser(HttpSession session,
                                       @PathVariable("type") String type,
                                       @PathVariable("value") String value) {

        log.info("memberController.......");
        log.info("type={}", type);
        log.info("value={}", value);
        int count = memberService.selectCountMember(type, value);

        log.info("count={}", count);

        if (type.equals("email") && count <= 0) {
            log.info("email={}", value);
            memberService.sendEmailCode(session, value);
        }

        //Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }
    @ResponseBody
    @GetMapping("/seller/check/{type}/{value}")
    public ResponseEntity<?> checkSeller(HttpSession session,
                                       @PathVariable("type") String type,
                                       @PathVariable("value") String value) {

        log.info("memberController.......");
        log.info("type={}", type);
        log.info("value={}", value);
        int count = memberService.selectCountSeller(type, value);

        log.info("count={}", count);

        if (type.equals("sellerEmail") && count <= 0) {
            log.info("sellerEmail={}", value);
            memberService.sendEmailCode(session, value);
        }

        //Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }

    //findId
    @ResponseBody
    @GetMapping("/member/send/{email}/{name}/{hp}")
    public ResponseEntity<?> findId(HttpSession session,
                                    @PathVariable("email") String email
            , @PathVariable("name") String name, @PathVariable("hp") String hp) {

        log.info("memberController.......");
        log.info("email={}", email);
        log.info("name={}", name);
        log.info("hp={}", hp);

        int count = memberService.findMember(email, name, hp);//판매자, 일반사용자 모두 조회

        log.info("count={}", count);

        if (count > 0) {
            log.info("email={}", email);
            memberService.sendEmailCode(session, email);
        }

        //Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }


    //findId - 이메일 인증코드 검사
    @ResponseBody
    @GetMapping("/member/email/{code}")
    public ResponseEntity<?> checkEmailCode(HttpSession session, @PathVariable("code") String code) {
        String sessionCode = (String) session.getAttribute("code");

        log.info("시스템에서 보낸 code : " + sessionCode);

        log.info("내가 입력한 code : " + code);

        Map<String, Object> resultMap = new HashMap<>();

        if (sessionCode.equals(code)) {
            //Json 생성
            log.info("여기는 잘 입력한 쪽");
            resultMap.put("result", true);
        } else {
            //Json 생성
            log.info("여기는 잘 못 입력한 쪽");
            resultMap.put("result", false);
        }
        return ResponseEntity.ok().body(resultMap);
    }


    //회원가입 진행
    @PostMapping("/member/register")
    public String register(HttpSession session, HttpServletRequest req, UserDTO userDTO, Model model) {

        userDTO.setRegip(req.getRemoteAddr());
        userDTO.setSms((String) session.getAttribute("sms"));


        //session 선택 동의여부, 이메일 인증 코드 session 삭제
        session.removeAttribute("sms");
        session.removeAttribute("code");
        memberService.insert(userDTO);
        model.addAttribute("message", "회원가입이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/lotteshop/member/login");
        return "/member/message";
    }

    //약관 동의 페이지 매핑
    @GetMapping("/member/signup")
    public String signup(Model model) {
        TermsDTO terms = memberService.findTerms(1);
        model.addAttribute("terms", terms);
        return "/member/signup";
    }

    //약관 동의 선택동의 사항 체크 여부 session에 저장
    @PostMapping("/member/signup")
    public String signUp(HttpSession session, String agree4) {

        //session 값 설정
        session.setAttribute("sms", agree4);
        session.setAttribute("terms", "yes");
        return "redirect:/member/register";
    }

    /**
     * 여기부터는 판매자
     */

    //약관 동의 페이지 매핑
    @GetMapping("/member/signupseller")
    public String signupSeller(Model model) {
        Policy policy = policyService.buyerPolicy(2L);
        model.addAttribute("policy", policy);
        return "/member/signupSeller";
    }

    @PostMapping("/member/signupseller")
    public String signupSeller(HttpSession session) {
        session.setAttribute("termsSeller","yes");

        return "redirect:/member/registerseller";
    }

    //판매자 회원가입 페이지 매핑
    @GetMapping("/member/registerseller")
    public String registerSeller(HttpSession session) {
        if(session.getAttribute("termsSeller") == null){
            return "redirect:/member/signupseller";
        }
        return "/member/registerSeller";
    }

    @PostMapping("/member/registerseller")
    public String registerSeller(HttpSession session, SellerDTO sellerDTO, Model model) {


        memberService.insert(sellerDTO);
        model.addAttribute("message", "판매자 회원가입이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/lotteshop/member/login");
        return "/member/message";
    }

    //아이디 찾기 페이지
    @GetMapping("/member/findId")
    public String findId() {

        return "/member/findId";
    }

    //아이디 찾기 결과가 보이는 페이지
    @GetMapping("/member/findIdResult")
    public String findResult(UserDTO userDTO, Model model, @RequestParam("userType") int userType) {

        log.info("아이디 찾기 결과 페이지입니다.");
        log.info("userDTO={}", userDTO);
        log.info("userType :" + userType);

        String uid = memberService.findId(userDTO, userType);

        model.addAttribute("uid", uid);

        return "/member/findIdResult";
    }

    //비밀번호 찾기 페이지
    @GetMapping("/member/findPass")
    public String findPass() {


        return "/member/findPass";
    }


    //비밀번호설정을 위해 아이디가 존재하는지 확인
    @GetMapping("/member/passResult/{uid}/{email}/{isSeller}")
    public ResponseEntity<?> passResult(@PathVariable("uid") String uid
            , @PathVariable("email") String email, @PathVariable("isSeller") boolean isSeller) {

        boolean pass = memberService.findPass(uid, email);

        log.info("isSeller값 : " + isSeller);

        Map<String, Object> resultMap = new HashMap<>();

        if (pass) {

            log.info("사용자가 존재합니다.!");

            memberService.tempPass(email, uid,isSeller);


            resultMap.put("result", "true");
        } else {

            log.info("사용자가 없습니다.");

            resultMap.put("result", "false");
        }

        return ResponseEntity.ok().body(resultMap);
    }


}
