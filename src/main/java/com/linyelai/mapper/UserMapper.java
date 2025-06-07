package com.linyelai.mapper;

import com.linyelai.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

     UserPO findByUsername(@Param("username")String username);
     int addUser(@Param("user")UserPO userPO);
     int updateUser(@Param("user")UserPO userPO);
     int deleteUser(@Param("id")Long id);
}
