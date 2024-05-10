package kr.co.lotte.service;

import groovy.lang.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.entity.BlogImg;
import kr.co.lotte.entity.Products;
import kr.co.lotte.entity.ReviewImg;
import kr.co.lotte.repository.BlogImgRepository;
import kr.co.lotte.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    public List<Blog> findAll(){
        return blogRepository.findAll();
    }


    public BlogPageResponseDTO findAllList(BlogPageRequestDTO blogPageRequestDTO) {
        Pageable pageable = blogPageRequestDTO.getPageable("no");


       Page<Blog> page=  blogRepository.findAllList(blogPageRequestDTO,pageable);//블로그에 있는 내용 전부다 들고오기

        List<Blog> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new BlogPageResponseDTO(blogPageRequestDTO, dtoList , total);
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
        Map<String, Object> map = new HashMap<>();
        try {
            Blog blog = modelMapper.map(blogDTO, Blog.class);

            MultipartFile img1 = blogDTO.getMultImage1();
            if(img1.getOriginalFilename() != null && img1.getOriginalFilename() != "") {
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
                map.put("data", blog1.getBno());//글 등록 번호
                return ResponseEntity.ok().body(map);
            }else{
                Blog originBlog = blogRepository.findById(blog.getBno()).get();
                blog.setImages(originBlog.getImages());
                Blog blog1 = blogRepository.save(blog);
                map.put("data", blog1.getBno());
                return ResponseEntity.ok().body(map);
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }


    public BlogImgDTO uploadReviewImage(MultipartFile file) {
        // 파일을 저장할 경로 설정

        String path = new java.io.File(fileUploadPath).getAbsolutePath();


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
                        .size(330, 230)//여기를 size에서 forceSize로 강제 사이즈 변환
                        .toFile(dest);


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
    
    //블로그 글 삭제
    @Transactional
    public void blogDel(int bno){

        log.info("여기는 blogService - blogDel - bno :"+bno);
        
        //uploads에 있는 이미지 삭제
        Optional<Blog> optBlog = blogRepository.findById(bno);

        Blog blog = modelMapper.map(optBlog,Blog.class);

        String img = blog.getImages();//이미지 이름

        delBlogImg(img);//이미지 삭제

        log.info("이미지 삭제 오케?");

        //blogImg에 있는 데이터 삭제
        blogImgRepository.deleteByBno(bno);

        log.info("데이터 삭제 오케?");
        
        //블로그 테이블에서 삭제
       blogRepository.deleteById(bno);

        log.info("블로그 글 삭제 오케?");
        
    }

    //실질적인 이미지 파일 삭제 함수
    public void delBlogImg(String img){

        String path = new java.io.File(fileUploadPath).getAbsolutePath();

        java.io.File dest = new File(path, img);

        dest.delete();

    }

    //수정할 블로그 글을 가져오기
    public Blog findBlog(int bno){

       Optional<Blog> optBlog = blogRepository.findById(bno);

       return modelMapper.map(optBlog,Blog.class);

    }

    //수정하고 저장
    @Transactional
    public void modifyBlog(BlogDTO blogDTO){

        log.info("수정하고 다시 저장 : "+blogDTO.getBno());
        if(blogDTO.getMultImage1().getOriginalFilename() != null && blogDTO.getMultImage1().getOriginalFilename() !="" ){
            //원래 있었던 이미지 먼저 삭제
            try{
                BlogImg blogImg = blogImgRepository.findByBno(blogDTO.getBno());

                String deleImg = blogImg.getSName();//삭제 이미지 이름

                log.info("삭제 이미지 이름 : "+deleImg);

                if(deleImg.equals(blogDTO.getMultImage1())){}

                delBlogImg(deleImg);//이미지 삭제

                log.info("이미지 삭제 완료!");

                blogImgRepository.deleteByBno(blogDTO.getBno());//이미지 테이블에서도 삭제

                log.info("이미지테이블에서 삭제 완료!");
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }


        //새로 들어온 이미지를 리사이징 해서 저장
        bRegister(blogDTO);

        log.info("여기는 blogService - modifyBlog : 수정이 잘 되겠지?");

    }
}
