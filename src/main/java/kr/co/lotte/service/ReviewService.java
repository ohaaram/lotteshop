package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ProductsRepository productRepository;
    private final SubProductsRepository subProductsRepository;
    private final ModelMapper modelMapper;
    private final OrderItemsRepository orderItemsRepository;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    // 리뷰 목록 조회
    public ReviewPageResponseDTO selectReviews(int prodno, ReviewPageRequestDTO reviewPageRequestDTO) {

        Pageable pageable = reviewPageRequestDTO.getPageable("no");
        log.info("selectReviews Serv ...1 ");
        // 리뷰 목록  Page 조회
        Page<Tuple> results = reviewRepository.selectReviewsAndNick(prodno, reviewPageRequestDTO, pageable);

        log.info("selectReviews Serv ...2 " + results.getTotalElements());
        log.info("selectReviews Serv ...3 " + results.getTotalPages());

        // Page<Tuple>을 List<ReviewDTO>로 변환
        List<ReviewDTO> reviewList = results.getContent().stream()
                .map(tuple -> {
                    // Tuple 에서 Entity GET
                    Review review = tuple.get(0, Review.class);
                    String nick = tuple.get(1, String.class);

                    // Entity -> DTO
                    ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
                    reviewDTO.setNick(nick);
                    log.info("selectReviews Serv ...3 : " + reviewDTO.toString());
                    return reviewDTO;
                }).toList();
        log.info("selectReviews Serv ...4 : " + reviewList.toString());

        // List<ReviewDTO>와 Page 리턴
        int total = (int) results.getTotalElements();
        return ReviewPageResponseDTO.builder()
                .pageRequestDTO(reviewPageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();
    }

    // 리뷰 avg, sum, count(*) 조회 + score별 count 조회
    @Transient
    public ReviewRatioDTO selectForRatio(int prodno) {

        // 리뷰 avg, sum, count(*) 조회 : 전체 데이터 기준으로 집계해야해서 group by 사용 불가 (score별 count 조회 따로 해야함)
        Tuple result = reviewRepository.selectForRatio(prodno);
        log.info("리뷰 집계 조회 ...1 : " + result);

        // JPA는 count는 long, sum은 int or long, avg는 double로 반환된다.
        long count = result.get(0, Long.class);
        double avg = 0;
        Integer sum = 0;

        // 리뷰가 하나도 없으면 에러가 발생하기 때문에 null 체크
        if (count > 0) {
            avg = result.get(1, Double.class);
            sum = result.get(2, Integer.class);
        }
        // 해당 점수를 한 번도 받지않으면 조회되지 않기 때문에 따로 선언
        int oneScore = 0;
        int twoScore = 0;
        int threeScore = 0;
        int fourScore = 0;
        int fiveScore = 0;

        // 리뷰 score별(GROUP BY score) 조회
        List<Tuple> groupResult = reviewRepository.selectScoreCount(prodno);
        for (Tuple tuple : groupResult) {
            log.info("리뷰 집계 조회 ...2 : " + tuple);
            int score = tuple.get(0, Integer.class);
            long scoreCountLong = tuple.get(1, Long.class);
            int scoreCount = (int) scoreCountLong;

            // 각 케이스에서 해당하는 변수에 값을 더해줌
            switch (score) {
                case 1:
                    oneScore += scoreCount;
                    break;
                case 2:
                    twoScore += scoreCount;
                    break;
                case 3:
                    threeScore += scoreCount;
                    break;
                case 4:
                    fourScore += scoreCount;
                    break;
                case 5:
                    fiveScore += scoreCount;
                    break;
            }
        }

        // Select 한 값 빌더로 입력
        return ReviewRatioDTO.builder()
                .count(count)
                .sum(sum)
                .avg(avg)
                .oneScore(oneScore)
                .twoScore(twoScore)
                .threeScore(threeScore)
                .fourScore(fourScore)
                .fiveScore(fiveScore)
                .build();

    }



    //리뷰의 각 점수의 합계를 구함
    public Map<Integer, Integer> sumScore(int prodno) {
        Optional<Products> optProducts = productRepository.findById(prodno);

        if (optProducts.isPresent()) {
            Products products = optProducts.get();
            List<Review> reviews = products.getReviews();
            Map<Integer, Integer> scoreCountMap = new HashMap<>();

            for (Review review : reviews) {
                int score = review.getScore();
                scoreCountMap.put(score, scoreCountMap.getOrDefault(score, 0) + 1);
            }

            // 각 점수가 없는 경우를 처리하여 0으로 초기화
            for (int i = 5; i >= 1; i--) {
                scoreCountMap.putIfAbsent(i, 0);
            }

            return scoreCountMap;
        } else {
            log.info("해당하는 상품이 없습니다.");
            return null;
        }
    }



    // 리뷰 작성 + 상품 테이블 recount up + file -> Thumbnails
    @Transient
    public String insertReview(ReviewDTO reviewDTO, MultipartFile thumb) {

        log.info("리뷰 업로드 insertReview1 reviewDTO : " + reviewDTO.toString());
        log.info("리뷰 업로드 insertReview2 이미지 : " + thumb);

        String path = new File(fileUploadPath).getAbsolutePath();
        String sName = null;
        // 이미지 리사이즈 120 * 120
        if (thumb != null && !thumb.isEmpty()) {
            try {
                // oName, sName 구하기
                String oName = thumb.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                sName = UUID.randomUUID().toString() + ext;
                log.info("insertReview oName : " + oName);
                log.info("insertReview sName : " + sName);


                String orgPath = path + "/orgImage";
                // 원본 파일 폴더 자동 생성
                File orgFile = new File(orgPath);
                if (!orgFile.exists()) {
                    orgFile.mkdir();
                }

                // 원본 파일 저장
                thumb.transferTo(new File(orgPath, sName));
                // 썸네일 생성 후 저장
                Thumbnails.of(new File(orgPath, sName)) // 원본 파일 (경로, 이름)
                        .size(120, 120) // 원하는 사이즈
                        .toFile(new File(path, sName)); // 생성한 이미지 저장

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        // DTO -> Entity
        reviewDTO.setThumbnail(sName);
        Review review = modelMapper.map(reviewDTO, Review.class);

        // review insert
        reviewRepository.save(review);

        // product recount ++
        Products product = productRepository.findById(review.getNproduct().getProdNo()).get();
        product.setRecount(product.getRecount() + 1);

        // product update
        productRepository.save(product);
        return null;
    }

    //작성한 리뷰를 저장함
    public ResponseEntity<?> rRegister(ReviewDTO reviewDTO) {
        try {

            int prodno = reviewDTO.getProdNo();

            log.info("review_service - prodNO : " + prodno);
            log.info("review_service - ReviewDTO : " + reviewDTO);

            Review review = modelMapper.map(reviewDTO, Review.class);

            log.info("DTO에서 엔티티로 변환후 : " + review);

            MultipartFile image1 = reviewDTO.getMultImage1();

            ReviewImgDTO uploadedImage = uploadReviewImage(image1);


            if (uploadedImage != null) {

                ReviewImgDTO imageDTO = uploadedImage;

                review.setThumbnail(uploadedImage.getSName());
            }

            Products product111 = new Products(); // 적절한 방법으로 상품 엔티티 객체를 생성

            // 해당 상품의 prodNo 값을 설정
            product111.setProdNo(prodno);

            // Review 엔티티의 nproduct 필드에 상품 엔티티 객체를 설정
            review.setNproduct(product111);
            log.info("service - rRegister : " + review);

            Review saveReview = reviewRepository.save(review);//새로운 리뷰 세이브

            log.info("새로운 리뷰 세이브 포인트1");

            Optional<Products> optProd = productRepository.findById(prodno);//상품번호를 이용해서 상품정보 가져오기

            log.info("새로운 리뷰 세이브 포인트2");

            Products product = modelMapper.map(optProd, Products.class);

            log.info("새로운 리뷰 세이브 포인트3");

            List<Review> Oreview = product.getReviews();//기존에 있었던 리뷰

            log.info("새로운 리뷰 세이브 포인트3- Oreview" + Oreview);

            Oreview.add(saveReview);//기존에 있었던 리뷰뒤에 새로운 리뷰 저장

            log.info("새로운 리뷰 세이브 포인트4(기존+새로움) - Oreview" + Oreview);

            product.setReviews(Oreview);//기존+새로운 리뷰 덮어씌워버리기

            product.calculateAvgRating();//평균 구하기

            log.info("구한 평균 불러보기 : "+product.getAvg());

            log.info("새로운 리뷰 세이브 포인트5 - product:" + product);

            productRepository.save(product);

            log.info("service - saveReview 저장성공?! : " + product);

            int saveReviewNo = saveReview.getRno();//리뷰저장하면 리뷰번호가 자동으로 생성됨. -> 그거 불러옴

            log.info("service - saveReviewNo : " + saveReviewNo);//리뷰번호 찍어보기

            ReviewImgDTO reviewImgDTO = uploadedImage;
            reviewImgDTO.setRno(saveReviewNo);

            ReviewImg reviewImg = modelMapper.map(reviewImgDTO, ReviewImg.class);

            reviewImgRepository.save(reviewImg);
            reviewRepository.flush();

            Map<String, Object> map = new HashMap<>();
            map.put("data", saveReviewNo);//리뷰 번호임

            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    //리뷰이미지 저장
    public ReviewImgDTO uploadReviewImage(MultipartFile file) {
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
                        .forceSize(80, 80)//여기를 size에서 forceSize로 강제 사이즈 변환
                        .toFile(dest);


                log.info("service - dest : " + dest);

                // 리뷰이미지 정보를 담은 DTO 생성 및 반환
                return ReviewImgDTO.builder()
                        .oName(originalFileName)
                        .sName(sName)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null; // 업로드 실패 시 null 반환
    }


    //내가 작성한 리뷰를 싹 다 불러오기(페이지네이션 추가)
    public ReviewPageResponseDTO findReview(String uid, ReviewPageRequestDTO reviewPageRequestDTO) {

        Pageable pageable = reviewPageRequestDTO.getPageable("no");

        log.info("여기는 reviewService입니다.");

        Page<Review> reviews = reviewRepository.findByUid(uid, reviewPageRequestDTO, pageable);

        log.info("review_service - findReview - reviews : " + reviews);
        List<Review> reviews1 = reviews.getContent();

        int total = (int) reviews.getTotalElements();
        return new ReviewPageResponseDTO(reviewPageRequestDTO, reviews.getSize(), reviews1);
    }

    //최근에 작성한 리뷰 5개만 뽑아오기
    public List<Review> find_five(String uid) {

        List<Review> reviews = reviewRepository.findTop5ByUidOrderByRdateDesc(uid);

        return reviews;
    }

    public String findOption(int itemno) {

         Optional<OrderItems> optOderItem = orderItemsRepository.findById(itemno);

         OrderItems orderItems = modelMapper.map(optOderItem,OrderItems.class);

         log.info("reviewService - findOption - orderItems(아이템번호로 조회한 orderItem 데이터들)"+orderItems);

         int subProductsId = orderItems.getProdNo();//subProducts의 id

        log.info("reviewService - findOption - subProductsId : "+subProductsId);

       Optional<SubProducts> optSubProducts =  subProductsRepository.findById(subProductsId);//subProdNo으로 데이터 불러오기

        SubProducts subProducts = modelMapper.map(optSubProducts,SubProducts.class);

        String option=null;
        String color=null;
        String size=null;

        if(subProducts.getColor()!=null && subProducts.getSize()!=null) {

            color = subProducts.getColor();

            size = subProducts.getSize();

            option = "컬러 : " + color + "/ 사이즈 : " + size;
        }else if(subProducts.getSize()!=null){

            size = subProducts.getSize();

            option = "사이즈 : "+size;

        }else  if(subProducts.getColor()!=null){
            color = subProducts.getColor();

            option = "컬러 : "+color;
        }else{
            option = "옵션 없음";
        }
        return option;
    }


    //리뷰 유효성 검사
    public int findByorderno(int orderno, int prodno, int itemno) {

        int count = reviewRepository.countByOrdernoAndNproductProdNoAndItemno(orderno, prodno, itemno);

        log.info("service - findByorderno - count : " + count);

        return count;
    }
}