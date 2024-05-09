package kr.co.lotte.mapper;

import kr.co.lotte.dto.SellerDTO;
import kr.co.lotte.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    public int selectCountMember(@Param("type") String type, @Param("value") String value);

    public UserDTO findUser(String uid);

    public void updateUserForType(@Param("type") String type, @Param("value") String value, String uid);

    public void updateUserAddr(UserDTO userDTO);

    public void updateUserPassword(UserDTO userDTO);
    public void updateSellerPassword(SellerDTO sellerDTO);

    public String findId(UserDTO userDTO);
    public String findId2(UserDTO userDTO);

    public int findPass1(String uid,String email);
    public int findPass2(String uid,String email);

    public int findMember1(String email,String name,String hp);
    public int findMember2(String email,String name,String hp);
}
