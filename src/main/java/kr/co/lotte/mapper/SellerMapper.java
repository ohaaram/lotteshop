package kr.co.lotte.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SellerMapper {

    public int selectCountSeller(@Param("type") String type, @Param("value") String value);
}
