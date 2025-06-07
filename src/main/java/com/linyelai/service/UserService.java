package com.linyelai.service;

import com.linyelai.po.UserPO;
import org.apache.ibatis.annotations.Param;

public interface UserService {

    public UserPO findByUsername(String username);
    public int addUser(UserPO userPO);
    public int updateUser(UserPO userPO);
    public int deleteUser(Long id);
}
