package kr.co.lotte.service;

import groovy.lang.Tuple;
import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.dto.BlogImgDTO;
import kr.co.lotte.dto.ReviewImgDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.BlogImg;
import kr.co.lotte.entity.ReviewImg;
import kr.co.lotte.repository.BlogImgRepository;
import kr.co.lotte.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogImgRepository blogImgRepository;
    private final ModelMapper modelMapper;

    //블로그에 저장되어 있는 글을 모두 들고오기
    public List<Blog> list() {

        return blogRepository.findTop5ByOrderByDateDesc();
    }


    //배너 번호에 있는 블로그 글 들고오기
    public BlogDTO findById(int bno) {
        Blog blog = blogRepository.findById(bno).orElse(null);

        return modelMapper.map(blog, BlogDTO.class);
    }

    public List<Blog> findAll() {

        return blogRepository.findAll();//블로그에 있는 내용 전부다 들고오기
    }

    //카테고리에 따른 블로그 내용들
    public List<Blog> selectBlogForStory(String cate) {
        List<Blog> card = null;

        if (cate.equals("전체")) {
            card = blogRepository.selectBlogForStory("");
        } else {
            card = blogRepository.selectBlogForStory(cate);
        }

        return card;
    }


    @Value("${file.upload.path}")
    private String fileUploadPath;

    //작성한 블로그글 저장
    public ResponseEntity<?> bRegister(BlogDTO blogDTO) {

        try {
            Blog blog = modelMapper.map(blogDTO, Blog.class);

            MultipartFile img1 = blogDTO.getMultImage1();

            BlogImgDTO uploadedImage = uploadReviewImage(img1);

            if (uploadedImage != null) {

                BlogImgDTO imageDTO = uploadedImage;

                blog.setImages(uploadedImage.getSName());
            }

            Blog blog1 = blogRepository.save(blog);


            BlogImgDTO ImgDTO = uploadedImage;
            ImgDTO.setBno(blog1.getBno());

            log.info("글 등록 번호임 : " + blog1.getBno());

            BlogImg blogImg = modelMapper.map(ImgDTO, BlogImg.class);

            blogImgRepository.save(blogImg);
            blogImgRepository.flush();

            Map<String, Object> map = new HashMap<>();
            map.put("data", blog1.getBno());//글 등록 번호

            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }


    public BlogImgDTO uploadReviewImage(MultipartFile file) {
        // 파일을 저장할 경로 설정

        String path = new java.io.File(fileUploadPath).getAbsolutePath();


        //file:///Users/ohara/Desktop/lotteon-team4/prodImg/"

        //String path2 = new java.io.File(fileUploadPath2).getAbsolutePath();


        if (!file.isEmpty()) {
            try {
                // 원본 파일 이름과 확장자 추출
                String originalFileName = file.getOriginalFilename();//원본 파일 네임
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

                log.info("uploadReviewImage - originalFileName :  잘 들어오나요? : " + originalFileName);

                // 저장될 파일 이름 생성
                String sName = UUID.randomUUID().toString() + extension;//변환된 파일 이름


                log.info("파일 변환 후 이름 - sName : " + sName);

                // 파일 저장 경로 설정
                java.io.File dest = new File(path, sName);

                Thumbnails.of(file.getInputStream())
                        .forceSize(80, 80)//여기를 size에서 forceSize로 강제 사이즈 변환
                        .toFile(dest);

                // 두 번째 파일 저장
                //java.io.File dest2 = new File(path2, sName);
                //Thumbnails.of(file.getInputStream()).forceSize(80, 80).toFile(dest2);


                log.info("service - dest : " + dest);

                // 리뷰이미지 정보를 담은 DTO 생성 및 반환
                return BlogImgDTO.builder()
                        .oName(originalFileName)
                        .sName(sName)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null; // 업로드 실패 시 null 반환
    }


    //블로그 넘버로 내용 들고 오기
    public Blog BlogFindById(int bno){

        Optional<Blog> blog = blogRepository.findById(bno);

        return modelMapper.map(blog,Blog.class);
    }

}
