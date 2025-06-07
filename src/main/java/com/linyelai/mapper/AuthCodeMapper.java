package com.linyelai.mapper;

import com.linyelai.po.AuthCodePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthCodeMapper {


     int addAuthCode(@Param("authCodePO")AuthCodePO authCodePO);

     AuthCodePO getAuthCode(@Param("authCode")String authCode);
}
