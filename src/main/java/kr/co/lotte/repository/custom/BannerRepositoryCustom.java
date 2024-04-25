package kr.co.lotte.repository.custom;

public interface BannerRepositoryCustom {

    Long countByPositionAndStatus(String position, int status);
}
