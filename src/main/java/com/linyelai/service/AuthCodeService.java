package com.linyelai.service;

import com.linyelai.po.AuthCodePO;

public interface AuthCodeService {

    int addAuthCode(String authCode);
    AuthCodePO getAuthCode(String authCode);
}
