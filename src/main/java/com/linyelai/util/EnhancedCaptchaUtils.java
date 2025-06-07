package com.linyelai.util;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class EnhancedCaptchaUtils extends CaptchaUtils {

    /**
     * 生成验证码图片并返回Base64编码
     * @param captcha 验证码文本
     * @param width 图片宽度
     * @param height 图片高度
     * @param format 图片格式（如"JPEG", "PNG"）
     * @return Base64编码的图片字符串
     */
    public static String generateImageCaptchaBase64(String captcha, int width, int height, String format) {
        try {
            java.awt.image.BufferedImage image = generateImageCaptcha(captcha, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, format, baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }

    /**
     * 生成验证码图片并返回Base64编码（带data URI scheme）
     * @param captcha 验证码文本
     * @param width 图片宽度
     * @param height 图片高度
     * @param format 图片格式（如"JPEG", "PNG"）
     * @return data URI格式的Base64图片字符串
     */
    public static String generateImageCaptchaDataURI(String captcha, int width, int height, String format) {
        String base64 = generateImageCaptchaBase64(captcha, width, height, format);
        return "data:image/" + format.toLowerCase() + ";base64," + base64;
    }
}