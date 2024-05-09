package kr.co.lotte.service;

import com.querydsl.core.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kr.co.lotte.repository.ProductsRepository;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.mapper.TermsMapper;
import kr.co.lotte.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.PanelUI;

import org.springframework.data.domain.Pageable;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private final BannerRepository bannerRepository;
    @Autowired
    private final BannerImgRepository bannerImgRepository;

    @Autowired
    private ProdImageRepository prodImageRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private SubProductsRepository subProductsRepository;
    @Autowired
    private OrdersItemRepository ordersItemRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private final Seller_statusRepository seller_statusRepository;
    @Autowired
    private TermsRepository termsRepository;
    @Autowired
    private TermsMapper termsMapper;

    //mainPage 띄우자
    public  Map<String , Integer> Formain(){
        Map<String , Integer> map = new HashMap<>();
        //주문업무
        int ready=0;
        int delivery = 0;
        int delete = 0;
        int allDelete =0;

        List<OrderItems> totalOrders = ordersItemRepository.findAll();
        //주문건수
        int count =totalOrders.size();
        for(OrderItems orderItems : totalOrders){
            if(orderItems.getOrderState().equals("주문 대기")){
                ready++;
            }else if(orderItems.getOrderState().equals("배송 준비")){
                delivery ++;

            }else if(orderItems.getOrderState().equals("주문 취소")){
                delete++;
            }else if(orderItems.getOrderState().equals("환불")){
                allDelete++;
            }
        }

        //주문금액
        int total = 0;


        List<Orders> orders = ordersRepository.findAll();
        for (Orders orders1 : orders){
            total+= orders1.getOrderTotalPrice();
            }

        //회원가입
        int users = memberRepository.findAll().size();


        //쇼핑몰방문 (일일)
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        int visitors = visitorRepository.findById(formattedDate).get().getVisitCount();

        //신규게시물 (미완)
        //고객 문의 (미완)

        //주문업무현황
        //주문대기
        //배송준비
        //취소요청
        //교환신청
        //반품요청
        map.put("count", count);
        map.put("total", total);
        map.put("user", users);
        map.put("ready",ready);
        map.put("delivery",delivery);
        map.put("delete",delete);
        map.put("allDelete",allDelete);
        map.put("visitors",visitors);


        return map;
    }

    //판매자 스토어용
    public  Map<String , Integer>  forManager(String storUid){
        Map<String , Integer> map = new HashMap<>();

        List<OrderItems> totalOrders = ordersItemRepository.findAll();
        //주문건수
        //주문금액
        int count = 0;
        int total = 0;

        //주문업무
        int ready=0;
        int delivery = 0;
        int delete = 0;
        int allDelete =0;

        for(OrderItems orderItems : totalOrders){

            if(orderItems.getOrderState().equals("주문 대기")){
                ready++;
            }else if(orderItems.getOrderState().equals("배송 준비")){
                delivery ++;
            }else if(orderItems.getOrderState().equals("주문 취소")){
                delete++;
            }else if(orderItems.getOrderState().equals("환불")){
                allDelete++;
            }

            int prodNo =subProductsRepository.findById(orderItems.getProdNo()).get().getProdNo();
            if(productsRepository.findById(prodNo).get().getSellerUid().equals(storUid)){
                Orders orders = ordersRepository.findById(orderItems.getOrderNo()).get();
                total += orders.getOrderTotalPrice();
                count++;
            }
        }



        //회원가입
        int users = memberRepository.findAll().size();

        //쇼핑몰방문 (일일)
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        int visitors = visitorRepository.findById(formattedDate).get().getVisitCount();

        //신규게시물
        map.put("count", count);
        map.put("total", total);
        map.put("user", users);
        map.put("ready",ready);
        map.put("delivery",delivery);
        map.put("delete",delete);
        map.put("allDelete",allDelete);

        map.put("visitors",visitors);
        return map;
    }

    //카테고리 조회 method들
    public List<Categories> searchCategories() {
        return categoriesRepository.findAll();
    }

    public ResponseEntity searchCategoriesSecondNames(String cate) {
        Set<String> names = categoriesRepository.findAllByCateName(cate).stream().map(categories -> categories.getSecondCateName()).collect(Collectors.toSet());
        List<String> namesList = names.stream().sorted().collect(Collectors.toList());
        log.info("searchCategoriesSecondNames {}!!", names);
        Map<String, List<String>> map = new HashMap<>();
        map.put("data", namesList);
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity searchCategoriesThridNames(String cate) {
        Set<String> names = categoriesRepository.findAllBySecondCateName(cate).stream().map(categories -> categories.getThirdCateName()).collect(Collectors.toSet());
        List<String> namesList = names.stream().sorted().collect(Collectors.toList());
        log.info("searchCategoriesSecondNames {}!!", names);
        Map<String, List<String>> map = new HashMap<>();
        map.put("data", namesList);
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity productRegister(ProductsDTO productsDTO) {

        Products products = modelMapper.map(productsDTO, Products.class);


        MultipartFile image3 = productsDTO.getMultImage3();
        MultipartFile image4 = productsDTO.getMultImage4();

        List<MultipartFile> files = List.of(image3, image4);

        List<ProdImageDTO> uploadedImages = fileUpload(files);
        //image1,2,3 set 해서 sname 넣기.
        if (!uploadedImages.isEmpty()) {
            for (int i = 0; i < uploadedImages.size(); i++) {
                ProdImageDTO imageDTO = uploadedImages.get(i);
                if (i == 0) {
                    products.setImage1(imageDTO.getSName());
                } else if (i == 1) {
                    products.setImage2(imageDTO.getSName());
                } else if (i == 2) {
                    products.setImage3(imageDTO.getSName());
                } else {
                    products.setImage4(imageDTO.getSName());
                }
            }
        }

        log.info("registerProd....1" + products);
        String sellerName = sellerRepository.findById(products.getSellerUid()).get().getSellerName();
        log.info("registerProd....2!!?" + sellerName);
        products.setSellerName(sellerName);
        log.info("registerProd....!!!!" + products);
        List<SubProducts> subProducts = subProductsRepository.findAllByProdNo(0);
        products.setProdPrice(subProducts.get(0).getProdPrice());
        int point = (int) (subProducts.get(0).getProdPrice() * 0.01);
        products.setPoint(point);
        Products savedProduct = productsRepository.save(products);
        log.info("registerProd....2" + savedProduct);
        int saveProdNo = savedProduct.getProdNo();
        log.info("registerProd....3" + saveProdNo);
        log.info("registerProd....3" + subProducts);

        for (SubProducts subProducts1 : subProducts) {
            subProducts1.setProdNo(saveProdNo);
            subProductsRepository.save(subProducts1);
        }


        for (ProdImageDTO prodImageDTO : uploadedImages) {
            prodImageDTO.setPNo(savedProduct.getProdNo());

            ProdImage prodImage = modelMapper.map(prodImageDTO, ProdImage.class);
            prodImageRepository.save(prodImage);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("data", savedProduct.getProdNo());
        return ResponseEntity.ok().body(map);

    }

    public ResponseEntity productModify(ProductsDTO productsDTO)  {
        Products products =modelMapper.map(productsDTO, Products.class);
        Products old = productsRepository.findById(products.getProdNo()).get();
        MultipartFile image3 = productsDTO.getMultImage3();
        MultipartFile image4 = productsDTO.getMultImage4();

        if(image3.getOriginalFilename() == null || image3.getOriginalFilename() == ""){
            log.info("here...");
            products.setImage1(old.getImage1());
            products.setImage2(old.getImage2());
            products.setImage3(old.getImage3());
        }else{
            try {
                List<ProdImageDTO> lists = fileModifyOne(image3);
                for (int i = 0; i < lists.size(); i++) {
                    ProdImageDTO imageDTO = lists.get(i);
                    if (i == 0) {
                        products.setImage1(imageDTO.getSName());
                    } else if (i == 1) {
                        products.setImage2(imageDTO.getSName());
                    } else if (i == 2) {
                        products.setImage3(imageDTO.getSName());
                    }
                }
            }catch (Exception e){

            }
        }

        if(image4.getOriginalFilename() == null || image4.getOriginalFilename() == ""){
            products.setImage4(old.getImage4());
        }else{
            try {
             ProdImageDTO image = fileModifyTwo(image4);
                products.setImage4(image.getSName());
            }catch (Exception e){

            }
        }
        products.setRegProdDate(old.getRegProdDate());
        productsRepository.save(products);

        Map<String, Integer> map = new HashMap<>();
        return ResponseEntity.ok().body(map);
    }


    //여기 하다가 말았음!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //(아래는)파일 크기 조정
    @Value("${file.upload.path}")
    private String fileUploadPath;

    public List<ProdImageDTO> fileModifyOne(MultipartFile files) throws IOException {

        if (fileUploadPath.startsWith("file:")) {

            fileUploadPath = fileUploadPath.substring("file:".length());
        }
        ;

        String path = new File(fileUploadPath).getAbsolutePath();

        // 이미지 정보 리턴을 위한 리스트
        List<ProdImageDTO> imageDTOS = new ArrayList<>();

        MultipartFile mf = files;
          String oName = mf.getOriginalFilename();
         String ext = oName.substring(oName.lastIndexOf(".")); // 확장자
           String sName = UUID.randomUUID().toString() + ext;
         File file = new File(path, sName);
                        Thumbnails.of(mf.getInputStream())
                                .size(190, 190) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();

                        oName = mf.getOriginalFilename();
                        ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        sName = UUID.randomUUID().toString() + ext;
                        file = new File(path, sName);
                        Thumbnails.of(mf.getInputStream())
                                .size(230, 230) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO2 = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();


                        oName = mf.getOriginalFilename();
                        ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        sName = UUID.randomUUID().toString() + ext;
                        file = new File(path, sName);
                        Thumbnails.of(mf.getInputStream())
                                .size(456, 456) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO3 = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();

                        imageDTOS.add(prodImageDTO);
                        imageDTOS.add(prodImageDTO2);
                        imageDTOS.add(prodImageDTO3);
        return imageDTOS;
    }

    public ProdImageDTO fileModifyTwo(MultipartFile files) throws IOException {

        if (fileUploadPath.startsWith("file:")) {

            fileUploadPath = fileUploadPath.substring("file:".length());
        }
        ;

        String path = new File(fileUploadPath).getAbsolutePath();

        // 이미지 정보 리턴을 위한 리스트
        MultipartFile mf=files;
        String oName = mf.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf(".")); // 확장자
        String sName = UUID.randomUUID().toString() + ext;
        File file = new File(path, sName);

        Thumbnails.of(mf.getInputStream())
                .width(940)
                .keepAspectRatio(true)// 썸네일 크기 지정
                .toFile(file);

        ProdImageDTO prodImageDTO = ProdImageDTO.builder()
                .oName(oName)
                .sName(sName)
                .build();

        return  prodImageDTO ;
    }

    public List<ProdImageDTO> fileUpload(List<MultipartFile> files) {

        if (fileUploadPath.startsWith("file:")) {

            fileUploadPath = fileUploadPath.substring("file:".length());
        }
        ;

        String path = new File(fileUploadPath).getAbsolutePath();

        // 이미지 정보 리턴을 위한 리스트
        List<ProdImageDTO> imageDTOS = new ArrayList<>();

        log.info("fileUploadPath..1 : " + path);
        for (int i = 0; i < files.size(); i++) {
            MultipartFile mf = files.get(i);
            if(mf.getOriginalFilename() ==null || mf.getOriginalFilename() == "")
            {

            }else {
                try {

                    if (i == 0) {
                        String oName = mf.getOriginalFilename();
                        String ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        String sName = UUID.randomUUID().toString() + ext;
                        File file = new File(path, sName);


                        Thumbnails.of(mf.getInputStream())
                                .size(190, 190) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();


                        oName = mf.getOriginalFilename();
                        ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        sName = UUID.randomUUID().toString() + ext;
                        file = new File(path, sName);
                        Thumbnails.of(mf.getInputStream())
                                .size(230, 230) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO2 = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();


                        oName = mf.getOriginalFilename();
                        ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        sName = UUID.randomUUID().toString() + ext;
                        file = new File(path, sName);
                        Thumbnails.of(mf.getInputStream())
                                .size(456, 456) // 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO3 = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();

                        imageDTOS.add(prodImageDTO);
                        imageDTOS.add(prodImageDTO2);
                        imageDTOS.add(prodImageDTO3);

                    } else {
                        String oName = mf.getOriginalFilename();
                        String ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                        String sName = UUID.randomUUID().toString() + ext;
                        File file = new File(path, sName);

                        Thumbnails.of(mf.getInputStream())
                                .width(940)
                                .keepAspectRatio(true)// 썸네일 크기 지정
                                .toFile(file);

                        ProdImageDTO prodImageDTO = ProdImageDTO.builder()
                                .oName(oName)
                                .sName(sName)
                                .build();

                        imageDTOS.add(prodImageDTO);
                    }

                    // 파일 정보 생성(imageDB에 저장될 DTO)
                } catch (IOException e) {
                    log.error("Failed to upload file: " + e.getMessage());
                }
            };


        }

        return imageDTOS;
    }

    public ResponseEntity insertSubOptions(List<SubProductsDTO> subProductsDTOS) {
        for (SubProductsDTO subProductsDTO : subProductsDTOS) {
            subProductsRepository.save(modelMapper.map(subProductsDTO, SubProducts.class));
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("data", 1);
        return ResponseEntity.ok().body(map);

    }

    public ProductsPageResponseDTO searchProducts(ProductsPageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Tuple> page = productsRepository.searchAllProductsForAdmin(requestDTO, pageable);
        log.info(page.getContent().toString() + "!!!!?!");
        List<SubProducts> dtoList = page.getContent().stream()
                .map(tuple -> {
                    Products products = new Products();
                    products = tuple.get(0, Products.class);
                    SubProducts ss = tuple.get(1, SubProducts.class);
                    ss.setProducts(products);
                    return ss;
                }).toList();
        int total = (int) page.getTotalElements();
        return new ProductsPageResponseDTO(requestDTO, total, dtoList);
    }

    public ProductsPageResponseDTO searchProductsForManager(ProductsPageRequestDTO requestDTO, String uid) {
        Pageable pageable = requestDTO.getPageable("no");
        Page<Tuple> page = productsRepository.searchAllProductsForAdmin(requestDTO, pageable, uid);
        log.info(page.getContent().toString() + "!!!!?!");
        List<SubProducts> dtoList = page.getContent().stream()
                .map(tuple -> {
                    Products products = new Products();
                    products = tuple.get(0, Products.class);
                    SubProducts ss = tuple.get(1, SubProducts.class);
                    ss.setProducts(products);

                    log.info("여기는 판매자 전용 adminService - searchProductsForManager : "+ ss.getProducts());

                    return ss;
                }).toList();
        int total = (int) page.getTotalElements();
        return new ProductsPageResponseDTO(requestDTO, total, dtoList);

    }

    public Products findOnlyOneProduct(int productNo) {
        return productsRepository.findById(productNo).get();
    }

    public List<SubProducts> subProductsFind(int prodNo) {
        return subProductsRepository.findAllByProdNo(prodNo);
    }

    public void deleteProducts(List<Integer> subProductsNos) {
        for (Integer subProductsNo : subProductsNos) {
            //table에서 삭제
            int prodNo = subProductsRepository.findById(subProductsNo).get().getProdNo();

            subProductsRepository.deleteById(subProductsNo);

            try {
                List<SubProducts> list = subProductsRepository.findAllByProdNo(prodNo);
                int b = list.get(0).getProdPrice();
            } catch (Exception e) {
                deleteFile(prodNo);
                productsRepository.deleteById(prodNo);
            }
            //만약 subProducts가 하나도 없으면 거기서도 삭제 (uploads 파일 + prodImg + products entity)
        }
    }

    public void deleteProduct(int subProductsNo) {
        //table에서 삭제
        int prodNo = subProductsRepository.findById(subProductsNo).get().getProdNo();
        subProductsRepository.deleteById(subProductsNo);
        try {
            List<SubProducts> list = subProductsRepository.findAllByProdNo(prodNo);
            int b = list.get(0).getProdPrice();
        } catch (Exception e) {
            deleteFile(prodNo);
            productsRepository.deleteById(prodNo);
        }
        //만약 subProducts가 하나도 없으면 거기서도 삭제 (uploads 파일 + prodImg + products entity)
    }

    public void deleteFile(int prodNo) {
        List<ProdImage> prodImages = prodImageRepository.findBypNo(prodNo);
        for (ProdImage prodImage : prodImages) {
            // 업로드 디렉토리 파일 삭제
            String path = new File(fileUploadPath).getAbsolutePath();
            // 파일 객체 생성
            File file = new File(path + File.separator + prodImage.getSName());
            // 파일 삭제
            if (file.exists()) {
                file.delete();
            }
            prodImageRepository.delete(prodImage);
        }
    }


    //terms
    public TermsDTO findTerms(int intPk) {

        return termsMapper.findTerms(intPk);
    }

    public void modifyTerms(TermsDTO termsDTO) {
        log.info(termsDTO.getTerms2());
        log.info(termsDTO.getIntPk() + "");
        termsMapper.modifyTerms(termsDTO);
    }

    @Transactional
    public ResponseEntity<?> bannerRegister(BannerDTO bannerDTO) {
        try {

            Banner banner = modelMapper.map(bannerDTO, Banner.class);//배너를 엔티티로 변환

            MultipartFile image1 = bannerDTO.getMultImage1();

            BannerImgDTO uploadedImage = uploadBannerImage(image1, banner.getPosition());

            if (uploadedImage != null) {

                BannerImgDTO imageDTO = uploadedImage;

                if (banner.getPosition().equals("MAIN1")) {
                    banner.setImg_1200(uploadedImage.getSName());
                } else if (banner.getPosition().equals("MAIN2")) {
                    banner.setImg_985(uploadedImage.getSName());
                } else if (banner.getPosition().equals("PRODUCT1")) {
                    banner.setImg_456(uploadedImage.getSName());
                } else if (banner.getPosition().equals("MEMBER1")) {
                    banner.setImg_425(uploadedImage.getSName());
                } else {
                    banner.setImg_810(uploadedImage.getSName());
                }

            }

            log.info("registerBanner-position : " + banner.getPosition());

            log.info("registerBanner....1 : : " + banner);


            Banner savedBanner = bannerRepository.save(banner);
            log.info("Banner 저장 성공: {}", savedBanner);


            log.info("registerBanner....2" + savedBanner);

            int saveBannerNo = savedBanner.getBannerNo();

            log.info("registerBanner....3" + saveBannerNo);

            BannerImgDTO bannerImgDTO = uploadedImage;
            bannerImgDTO.setBno(savedBanner.getBannerNo());//bannerImgDTO에 배너번호 집어넣기

            BannerImg bannerImg = modelMapper.map(bannerImgDTO, BannerImg.class);

            bannerImgRepository.save(bannerImg);
            bannerRepository.flush();

        Map<String, Integer> map = new HashMap<>();
        map.put("data", savedBanner.getBannerNo());

        return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }


    public BannerImgDTO uploadBannerImage(MultipartFile file, String position) {
        // 파일을 저장할 경로 설정

        String path = new File(fileUploadPath).getAbsolutePath();

        if (!file.isEmpty()) {
            try {
                // 원본 파일 이름과 확장자 추출
                String originalFileName = file.getOriginalFilename();//원본 파일 네임
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // 저장될 파일 이름 생성
                String sName = UUID.randomUUID().toString() + extension;//변환된 파일 이름


                // 파일 저장 경로 설정
                File dest = new File(path, sName);

                switch (position) {
                    case "MAIN1":

                        // 썸네일 생성 (이미지 크기를 1200x80으로 조정)
                        Thumbnails.of(file.getInputStream())
                                .forceSize(1200, 80)//여기를 size에서 forceSize로 강제 사이즈 변환
                                .toFile(dest);
                        break;
                    case "MAIN2":
                        // 썸네일 생성 (이미지 크기를 985 x 447으로 조정)
                        Thumbnails.of(file.getInputStream())
                                .forceSize(985, 447)//여기를 size에서 forceSize로 강제 사이즈 변환
                                .toFile(dest);
                        break;
                    case "PRODUCT1":
                        //456 x 60
                        Thumbnails.of(file.getInputStream())
                                .forceSize(456, 60)//여기를 size에서 forceSize로 강제 사이즈 변환
                                .toFile(dest);
                        break;
                    case "MEMBER1":
                        //425 x 260
                        Thumbnails.of(file.getInputStream())
                                .forceSize(425, 260)//여기를 size에서 forceSize로 강제 사이즈 변환
                                .toFile(dest);
                        break;
                    default:
                        //810 x 86(마이페이지)
                        Thumbnails.of(file.getInputStream())
                                .forceSize(810, 86)//여기를 size에서 forceSize로 강제 사이즈 변환
                                .toFile(dest);
                }


                // 배너 이미지 정보를 담은 DTO 생성 및 반환
                return BannerImgDTO.builder()
                        .oName(originalFileName)
                        .sName(sName)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null; // 업로드 실패 시 null 반환
    }

    //tab1에 대한 내용들
    public List<BannerDTO> findMAIN1(String main) {

        List<Banner> banners = bannerRepository.findByPosition(main);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    //tab2에 대한 내용들
    public List<BannerDTO> findMAIN2(String main) {

        List<Banner> banners = bannerRepository.findByPosition(main);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    //tab3에 대한 내용들
    public List<BannerDTO> findPRODUCT1(String main) {

        List<Banner> banners = bannerRepository.findByPosition(main);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    //tab4에 대한 내용들
    public List<BannerDTO> findMEMBER1(String main) {

        List<Banner> banners = bannerRepository.findByPosition(main);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    //tab5에 대한 내용들
    public List<BannerDTO> findMY1(String main) {

        List<Banner> banners = bannerRepository.findByPosition(main);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    //삭제버튼에 대한 내용
    @Transactional
    public void bannerDelete(String bannerNo) {

        int bno = Integer.parseInt(bannerNo);

        // 각 배너 번호에 대한 데이터 삭제!!!!!!!!!!!!
        //bannerImgRepository.deleteBybannerNo(bno);삭제할 때 오류가 나면 여기를 한번 더 확인하기!!!
        bannerRepository.deleteByBannerNo(bno);
        bannerImgRepository.deleteBybno(bno);//현상황 : bno를 이용해서 bannerImg테이블에서 삭제

    }


    //배너의 유효성 검사(시간, 날짜 등)->검사 후 유효하다면 status를 1로 저장해줌
    public BannerDTO findById(String bno) {

        int bNo = Integer.parseInt(bno);

        Optional<Banner> banner = bannerRepository.findById(bNo);

        BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);

        //포지션을 불러옴. 포지션을 가지고 같은 포지션 중에 status가 1인게 몇개인지, count해서 status가 1이상이면 status값 주지 말기(단, position이 main2는 5개 이하)

        String position = bannerDTO.getPosition();

        Long count = bannerRepository.countByPositionAndStatus(position, 1);

        // 현재 날짜
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 배너의 시작일과 종료일
        LocalDate startDate = LocalDate.parse(bannerDTO.getD_begin());
        LocalDate endDate = LocalDate.parse(bannerDTO.getD_end());

        // 배너의 시작 시간과 종료 시간
        LocalTime startTime = LocalTime.parse(bannerDTO.getT_begin());
        LocalTime endTime = LocalTime.parse(bannerDTO.getT_end());

        log.info("currentDate : " + currentDateTime);
        log.info("currentDateTime.toLocalDate() : " + currentDateTime.toLocalDate());
        log.info("startDate : " + startDate);
        log.info("endDate : " + endDate);

        // 배너의 시작일과 종료일
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        log.info("currentDateTime : " + currentDateTime);
        log.info("startDateTime : " + startDateTime);
        log.info("endDateTime : " + endDateTime);

        if (!position.equals("MAIN2") && count < 1) {

            // 현재 날짜와 시간이 배너의 기간에 포함되어 있는지 확인
            if (currentDateTime.isEqual(startDateTime) ||
                    (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime))) {
                // 배너를 노출합니다.
                log.info("여기로 들어와야해!!! 기간 안이라구!!!");

                bannerDTO.setStatus("1");

                Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

                bannerRepository.save(banner2);//status 변환한거 저장

                log.info("banner2 성공!: " + banner2);

                return bannerDTO;
            } else {
                // 배너 기간이 아닌 경우 노출하지 않습니다.
                log.info("기간 범위에 포함되지 않습니다.");
                bannerDTO.setStatus("0");

                Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

                bannerRepository.save(banner2);//status 변환한거 저장

                log.info("banner2 실패! : " + banner2);

                return null;
            }
        } else if (position.equals("MAIN2") && count < 5) {
            // 현재 날짜와 시간이 배너의 기간에 포함되어 있는지 확인
            if (currentDateTime.isEqual(startDateTime) ||
                    (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime))) {
                // 배너를 노출합니다.
                log.info("여기로 들어와야해!!! 기간 안이라구!!!");

                bannerDTO.setStatus("1");

                Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

                bannerRepository.save(banner2);//status 변환한거 저장

                log.info("banner2 성공!: " + banner2);

                return bannerDTO;
            } else {
                // 배너 기간이 아닌 경우 노출하지 않습니다.
                log.info("기간 범위에 포함되지 않습니다.");
                bannerDTO.setStatus("0");

                Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

                bannerRepository.save(banner2);//status 변환한거 저장

                log.info("banner2 실패! : " + banner2);

                return null;
            }
        } else {

            log.info("status 1의 값은 1개 이상이 될 수 없습니다.!");

            bannerDTO.setStatus("0");

            Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

            bannerRepository.save(banner2);//status 변환한거 저장

            log.info("banner2 실패! : " + banner2);
            return null;
        }
    }


    //status를 0으로 바꿔서 저장(비활성화 모드)
    public void findByIdForDelete(String bno) {

        int bNo = Integer.parseInt(bno);

        Optional<Banner> banner = bannerRepository.findById(bNo);

        BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);

        bannerDTO.setStatus("0");

        Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

        bannerRepository.save(banner2);

        log.info("findByIdForDelete - banner2 : " + banner2);
    }


    //조회수 카운트
    public Banner findByIdBanner(String bno) {
        int bNo = Integer.parseInt(bno);

        Optional<Banner> banner = bannerRepository.findById(bNo);

        BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);

        log.info("adminService-findByIdBanner : " + bannerDTO);

        bannerDTO.setHit(bannerDTO.getHit() + 1);

        log.info("adminService-findByIdBanner-bannerDTO " + bannerDTO);

        Banner banner2 = modelMapper.map(bannerDTO, Banner.class);

        bannerRepository.save(banner2);//조회수 +1하고 저장

        return banner2;
    }

    //주문현황
    public OrdersPageResponseDTO searchOrdersForManager(OrdersPageRequestDTO requestDTO, String uid) throws ParseException {
        Pageable pageable = requestDTO.getPageable("no");
        Page<OrderItems> page = ordersRepository.searchAllOrdersForManager(requestDTO,pageable,uid);
        List<OrderItems> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new OrdersPageResponseDTO(requestDTO,total, dtoList);
    }

    //주문현황
    public OrdersPageResponseDTO searchOrdersForAdmin(OrdersPageRequestDTO requestDTO) throws ParseException {
        Pageable pageable = requestDTO.getPageable("no");
        Page<OrderItems> page = ordersRepository.searchAllOrdersForAdmin(requestDTO , pageable);
        List<OrderItems> dtoList = page.getContent();
        int total = (int) page.getTotalElements();
        return new OrdersPageResponseDTO(requestDTO, total, dtoList);
    }

    //주문현황 별 상품 정보 출력
    public List<SubProducts> searchProductsForOrder(List<OrderItems> orderItems){
        List<SubProducts> list = new ArrayList<>();

        for(OrderItems order : orderItems){
            SubProducts subProducts =subProductsRepository.findById(order.getProdNo()).get();
            subProducts.setProdName(productsRepository.findById(subProducts.getProdNo()).get().getProdName());
            list.add(subProducts);
        }
        return list;
    }

    //주문상세 출력
    public Orders forOrderDetail(int orderNo){
        return ordersRepository.findById(orderNo).get();
    }



    //판매현황 출력
    public StatusPageResponseDTO seller_status(CsFaqPageRequestDTO pageRequestDTO){


        Pageable pageable = pageRequestDTO.getPageable("status_id");

       Page<Tuple> pageSeller_Status = seller_statusRepository.seller_status(pageRequestDTO ,pageable);

       List<Seller_statusDTO> dtoList = pageSeller_Status.getContent().stream()
               .map(tuple -> {

                   log.info("tuple : " + tuple);

                   Seller_statusDTO dto = new Seller_statusDTO();
                   dto.setSellerUid((String) tuple.get(0,String.class)); // 첫 번째 요소는 문자열로 캐스팅하여 id에 설정
                   dto.setOrderCount(tuple.get(1,Long.class)); // 두 번째 요소는 정수로 캐스팅하여 status에 설정
                   dto.setTotalPrice(tuple.get(2,Integer.class));

                   log.info("service - page - sellerDTO : " + dto);

                   return dto;
               })
               .toList();

       int total = (int) pageSeller_Status.getTotalElements();

       return StatusPageResponseDTO.builder()
               .pageRequestDTO(pageRequestDTO)
               .dtoList(dtoList)
               .total(total)
               .build();
    }

    //판매자 아이디를 이용해 판매자 정보 출력
    public SellerDTO findSellerInfo(String uid) {

        Optional<Seller> seller = sellerRepository.findById(uid);

        SellerDTO sellerDTO = modelMapper.map(seller, SellerDTO.class);

        return sellerDTO;
    }

    //주문상태변경
    public ResponseEntity changeOrderState(int orderNo){
        Map<String , String> map = new HashMap<>();
        //주문대기이면 배송준비로
        OrderItems orders = ordersItemRepository.findById(orderNo).get();
        if(orders.getOrderState().equals("주문 대기")){
            orders.setOrderState("배송 준비");
        }else if(orders.getOrderState().equals("배송 준비")){
            orders.setOrderState("배송 중");
        }
        map.put("data","1");

        return  ResponseEntity.ok().body(map);
    }

    public ResponseEntity changeOrderStates(List<Integer> itemNos){
        Map<String , String> map = new HashMap<>();
        //주문대기이면 배송준비로
        for(int orderNo : itemNos){
            OrderItems orders = ordersItemRepository.findById(orderNo).get();
            if(orders.getOrderState().equals("주문 대기")){
                orders.setOrderState("배송 준비");
            }else if(orders.getOrderState().equals("배송 준비")){
                orders.setOrderState("배송 중");
            }
        }

        map.put("data","1");

        return  ResponseEntity.ok().body(map);
    }

    //우리 매출현황을 해 보아요~ 이거는 pagination이 필요없다네~
    public List<String> forDays(String state){
       List<String> days = new ArrayList<>();
        if(state.equals("week")){
            LocalDate currentDate = LocalDate.now();
            for (int i = 6 ; i >= 0; i--) {
                LocalDate date = currentDate.minusDays(i);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
                days.add(dayName);
            }

            }else if(state.equals("month")){
                //현재 날짜를 가져오고
                LocalDate currentDate = LocalDate.now();

                // 한 달 전의 날짜를 계산
                LocalDate oneMonthAgo = currentDate.minusMonths(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
                for (LocalDate date = oneMonthAgo; date.compareTo(currentDate) <= 0; date = date.plusDays(1)){
                    log.info(formatter.format(date).toString() + "이거!");
                    days.add(formatter.format(date));
                }
            }else if(state.equals("year")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");

            LocalDate currentDate = LocalDate.now();
            LocalDate oneYearAgo = currentDate.minusYears(1);

            for (LocalDate date = oneYearAgo; !date.isAfter(currentDate); date = date.plusMonths(1)) {
                days.add(formatter.format(date));
            }
        }
        return days;
        }


    public Map<String , List<Integer>> saleForAdmin (String state) {
        Map<String , List<Integer>> map = new HashMap<>();
        //매출건수를 어떻게 조회하지?
        //일주일 별
        if(state.equals("week")){
            //주문건수
            List<Integer> listWeek = ordersRepository.searchOrdersWeekForAdmin();
            
            //매출액
            List<Integer> weekPrice = ordersRepository.searchPriceWeekForAdmin();

            map.put("order", listWeek);
            map.put("price", weekPrice);

        }
        //한 달
        if(state.equals("month")){
            List<Integer> listMonth = ordersRepository.searchOrdersMonthForAdmin();
            //매출액
            List<Integer> monthPrice = ordersRepository.searchPriceMonthForAdmin();

            map.put("order", listMonth);
            map.put("price", monthPrice);
        }

        //월 별
        if(state.equals("year")){
            List<Integer> listYear = ordersRepository.searchOrdersYearForAdmin();
            //매출액
            List<Integer> yearPrice = ordersRepository.searchPriceYearForAdmin();


            map.put("order", listYear);
            map.put("price", yearPrice);
        }
        return map;
        
    }

    public Map<String , List<Integer>> saleForManager(String state, String uid){
        Map<String , List<Integer>> map = new HashMap<>();
        //매출건수를 어떻게 조회하지?
        //일주일 별
        if(state.equals("week")){
            //주문건수
            List<Integer> listWeek = ordersRepository.searchOrdersWeekForManager(uid);

            //매출액
            List<Integer> weekPrice = ordersRepository.searchPriceWeekForManager(uid);

            map.put("order", listWeek);
            map.put("price", weekPrice);

        }
        //한 달
        if(state.equals("month")){
            List<Integer> listMonth = ordersRepository.searchOrdersMonthForManager(uid);
            //매출액
            List<Integer> monthPrice = ordersRepository.searchPriceMonthForManager(uid);

            map.put("order", listMonth);
            map.put("price", monthPrice);
        }

        //월 별
        if(state.equals("year")){
            List<Integer> listYear = ordersRepository.searchOrdersYearForManager(uid);
            //매출액
            List<Integer> yearPrice = ordersRepository.searchPriceYearForManager(uid);
            map.put("order", listYear);
            map.put("price", yearPrice);
        }
        return map;
    }
    
    //seller이면 true를 반환
    public Boolean findBySeller(String uid){

        // 판매자 검색
        Optional<Seller> seller = sellerRepository.findById(uid);

        return seller.isPresent();
    }

    //옵션수정
    public ResponseEntity modifyOptions(List<SubProductsDTO> subProductsDTOS){
        Map<String, String> map = new HashMap<>();

        for(SubProductsDTO subProductsDTO : subProductsDTOS){
            SubProducts old = subProductsRepository.findById(subProductsDTO.getSubProdNo()).get();
            old.setProdStock(subProductsDTO.getProdStock());
            old.setProdPrice(subProductsDTO.getProdPrice());
            subProductsRepository.save(old);
        }

        map.put("result", "1");
        return ResponseEntity.ok().body(map);
    }
}
