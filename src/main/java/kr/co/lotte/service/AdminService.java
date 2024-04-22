package kr.co.lotte.service;

import com.querydsl.core.Tuple;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductsRepository productsRepository;

    private final BannerRepository bannerRepository;

    private final BannerImgRepository bannerImgRepository;

    @Autowired
    private ProdImageRepository prodImageRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private SubProductsRepository subProductsRepository;


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


    //(아래는)파일 크기 조정

    @Value("${file.upload.path}")
    private String fileUploadPath;

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
            if (!mf.isEmpty()) {

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
            }
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

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private TermsMapper termsMapper;

    //terms
    public TermsDTO findTerms(int intPk) {

        return termsMapper.findTerms(intPk);
    }

    public void modifyTerms(TermsDTO termsDTO) {
        log.info(termsDTO.getTerms2());
        log.info(termsDTO.getIntPk() + "");
        termsMapper.modifyTerms(termsDTO);
    }

    public ResponseEntity<?> bannerRegister(BannerDTO bannerDTO) {

        Banner banner = modelMapper.map(bannerDTO, Banner.class);//배너를 엔티티로 변환

        MultipartFile image1 = bannerDTO.getMultImage1();

        BannerImgDTO uploadedImage = uploadBannerImage(image1);

        if (uploadedImage != null) {

            BannerImgDTO imageDTO = uploadedImage;

            banner.setImg_1200(uploadedImage.getSName()); //엔티티에 저장

        }

        log.info("registerBanner....1" + banner);

        Banner savedBannaer = bannerRepository.save(banner);

        log.info("registerBanner....2" + savedBannaer);

        int saveBannerNo = savedBannaer.getBannerNo();

        log.info("registerBanner....3" + saveBannerNo);

        BannerImgDTO bannerImgDTO = uploadedImage;
        bannerImgDTO.setBno(savedBannaer.getBannerNo());//bannerImgDTO에 배너번호 집어넣기

        BannerImg bannerImg = modelMapper.map(bannerImgDTO, BannerImg.class);

        bannerImgRepository.save(bannerImg);


        Map<String, Integer> map = new HashMap<>();
        map.put("data", savedBannaer.getBannerNo());
        return ResponseEntity.ok().body(map);

    }


    public BannerImgDTO uploadBannerImage(MultipartFile file) {
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

                // 썸네일 생성 (이미지 크기를 1200x80으로 조정)
                Thumbnails.of(file.getInputStream())
                        .forceSize(1200, 80)//여기를 size에서 forceSize로 강제 사이즈 변환
                        .toFile(dest);


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

    //삭제버튼에 대한 내용
    @Transactional
    public void bannerDelete(String bannerNo) {

        int bno = Integer.parseInt(bannerNo);

        // 각 배너 번호에 대한 데이터 삭제
        bannerRepository.deleteByBannerNo(bno);
        bannerImgRepository.deleteById(bno);

    }

    public BannerDTO findById(String bno) {

        int bNo = Integer.parseInt(bno);

        Optional<Banner> banner = bannerRepository.findById(bNo);

        return modelMapper.map(banner, BannerDTO.class);
    }

}
