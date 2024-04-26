package kr.co.lotte.service;

import kr.co.lotte.dto.BlogDTO;
import kr.co.lotte.entity.Blog;
import kr.co.lotte.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    //블로그에 저장되어 있는 글을 모두 들고오기
    public List<Blog> list(){

        return blogRepository.findTop5ByOrderByDateDesc();
    }


    //배너 번호에 있는 블로그 글 들고오기
    public BlogDTO findById(int bno){
        Blog blog = blogRepository.findById(bno).orElse(null);

        return modelMapper.map(blog, BlogDTO.class);
    }
}
