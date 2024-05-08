package kr.co.lotte.mapper;

import kr.co.lotte.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
    public void updateHit();
}
