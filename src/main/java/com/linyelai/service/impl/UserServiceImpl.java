package com.linyelai.service.impl;

import com.linyelai.mapper.UserMapper;
import com.linyelai.po.UserPO;
import com.linyelai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserPO findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public int addUser(UserPO userPO) {
        return userMapper.addUser(userPO);
    }

    @Override
    public int updateUser(UserPO userPO) {
        return userMapper.updateUser(userPO);
    }

    @Override
    public int deleteUser(Long id) {
        return userMapper.deleteUser(id);
    }
}
