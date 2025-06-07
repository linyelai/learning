package com.linyelai.controller;

import com.linyelai.controller.request.LoginRequest;
import com.linyelai.controller.request.SignUpRequest;
import com.linyelai.controller.response.LoginResponse;
import com.linyelai.controller.response.Response;
import com.linyelai.po.AuthCodePO;
import com.linyelai.po.UserPO;
import com.linyelai.service.AuthCodeService;
import com.linyelai.service.UserService;
import com.linyelai.util.DateUtil;
import com.linyelai.util.JwtTokenUtil;
import com.linyelai.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private AuthCodeService authCodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        Response<LoginResponse> loginResponseResponse = new Response<>();
        LoginResponse loginResponse = new LoginResponse();
        //valid
        String authCode = loginRequest.getAuthCode();
        if(StringUtils.containsWhitespace(authCode)){

            loginResponseResponse.setErrorCode("500002");
            loginResponseResponse.setErrorMsg("auth code can't be empty");
            return loginResponseResponse;

        }
        AuthCodePO authCodePO = authCodeService.getAuthCode(authCode);
        if (authCodePO == null) {
            loginResponseResponse.setErrorCode("500003");
            loginResponseResponse.setErrorMsg("auth code is invalid");
            return loginResponseResponse;
        }
        LocalDateTime expireTime = authCodePO.getExpiredTime();
        LocalDateTime currentTime = DateUtil.getCurrentDate();
        if (currentTime.compareTo(expireTime)>0) {
            loginResponseResponse.setErrorCode("500004");
            loginResponseResponse.setErrorMsg("wrong auth code");
            return loginResponseResponse;
        }
        // valid password
        String password = loginRequest.getPassword();
        String username = loginRequest.getUsername();
        if (StringUtils.isEmpty(username)) {
            loginResponseResponse.setErrorCode("500005");
            loginResponseResponse.setErrorMsg("username is empty");
            return loginResponseResponse;
        }
        if (StringUtils.isEmpty(password)) {
            loginResponseResponse.setErrorCode("500006");
            loginResponseResponse.setErrorMsg("password is empty");
            return loginResponseResponse;
        }
        String md5Password = PasswordUtils.hashPassword(password);
        // query by username
        UserPO userPO = userService.findByUsername(username);
        if (userPO == null) {
            loginResponseResponse.setErrorCode("500007");
            loginResponseResponse.setErrorMsg("user is not exist");
            return loginResponseResponse;
        }
        if (!md5Password.equals(userPO.getPassword())) {
            loginResponseResponse.setErrorCode("500008");
            loginResponseResponse.setErrorMsg("user is not exist");
            return loginResponseResponse;
        }
        // 生成 token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        loginResponse.setToken(token);
        loginResponseResponse.setBody(loginResponse);

        return loginResponseResponse;
    }

    @PostMapping("/signup")
    public  Response signup(@RequestBody SignUpRequest signUpRequest){
        Response response = new Response();
         UserPO userPO = new UserPO();
         String md5Password = PasswordUtils.hashPassword(signUpRequest.getPassword());
         userPO.setUsername(signUpRequest.getUsername());
         userPO.setPassword(md5Password);
         LocalDateTime localDateTime =  DateUtil.getCurrentDate();
         userPO.setCreateTime(localDateTime);
         userPO.setUpdateTime(localDateTime);
         userService.addUser(userPO);
        return response;
    }
}
