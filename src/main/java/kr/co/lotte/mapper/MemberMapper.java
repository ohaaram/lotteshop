package kr.co.lotte.mapper;

import kr.co.lotte.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    public int selectCountMember(@Param("type") String type, @Param("value") String value);
    public UserDTO findUser(String uid);

    public void updateUserForType(@Param("type") String type, @Param("value") String value, String uid);
}
