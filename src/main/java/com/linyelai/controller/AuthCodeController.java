package com.linyelai.controller;

import com.linyelai.service.AuthCodeService;
import com.linyelai.util.AppUtil;
import com.linyelai.controller.response.GetAuthCodeResponse;
import com.linyelai.controller.response.Response;
import com.linyelai.util.CaptchaUtils;
import com.linyelai.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@RestController
public class AuthCodeController {

    @Autowired
    private AuthCodeService authCodeService;

    @GetMapping("/authCode")
    public  Response  getAuthCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Response<String> response = new Response<>();
        // 生成图片并输出
        String captcha = CaptchaUtils.generateLetterCaptcha(6);
        //過期時間
        int result = authCodeService.addAuthCode(captcha);
        if(result<=0){
            response.setErrorCode("500001");
            response.setErrorMsg("failed to get auth code");
            return response;
        }

        java.awt.image.BufferedImage image = CaptchaUtils.generateImageCaptcha(captcha, 120, 40);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "JPEG", baos);
            String base64Str =  Base64.getEncoder().encodeToString(baos.toByteArray());
            response.setBody(base64Str);
        } catch (Exception e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
        return response;
    }
}
