package com.linyelai.service.impl;

import com.linyelai.mapper.AuthCodeMapper;
import com.linyelai.po.AuthCodePO;
import com.linyelai.service.AuthCodeService;
import com.linyelai.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthCodeServiceImpl implements AuthCodeService {
    @Autowired
    private AuthCodeMapper authCodeMapper;
    @Override
    public int addAuthCode(String authCode) {
        AuthCodePO authCodePO = new AuthCodePO();
        authCodePO.setAuthCode(authCode);
        LocalDateTime expireTime  = DateUtil.addTime(DateUtil.getCurrentDate(),60*5);
        LocalDateTime createTime  = DateUtil.getCurrentDate();
        authCodePO.setCreateTime(createTime);
        authCodePO.setExpiredTime(expireTime);
        return authCodeMapper.addAuthCode(authCodePO);
    }

    @Override
    public AuthCodePO getAuthCode(String authCode) {
        return authCodeMapper.getAuthCode(authCode);
    }
}
