package kr.co.lotte.service;

import kr.co.lotte.entity.Recruit;
import kr.co.lotte.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    public List<Recruit> findAll(){

        log.info("여기는 서비스단 - findAll");

       List<Recruit> lists = recruitRepository.findAll();//채용 정보 다 들고 오기

        log.info("RecruitService - List<Recruit> : " + lists);

        return lists;

    }

    public Recruit findById(int r_no){

        log.info("RecruitService-findById ");

        Optional<Recruit> r_noContent = recruitRepository.findById(r_no);

        Recruit recruit = modelMapper.map(r_noContent,Recruit.class);

        log.info("번호를 가지고 와서 내용 출력 : "+recruit.getR_content());

        return recruit;

    }

}
