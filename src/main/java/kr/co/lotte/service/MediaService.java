package kr.co.lotte.service;


import kr.co.lotte.entity.Media;
import kr.co.lotte.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    //영상 모두 가져오기
    public List<Media> findAll(){
        return mediaRepository.findAll();
    }


}
