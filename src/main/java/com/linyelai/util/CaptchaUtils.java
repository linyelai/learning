package com.linyelai.util;

import java.util.Random;

public class CaptchaUtils {

    private static final String NUMBER_CHARS = "0123456789";
    // 字母字符集
    private static final String LETTER_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    // 混合字符集（去除了容易混淆的字符）
    private static final String MIXED_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";

    /**
     * 生成纯数字验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateNumberCaptcha(int length) {
        return generateCaptcha(NUMBER_CHARS, length);
    }

    /**
     * 生成纯字母验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateLetterCaptcha(int length) {
        return generateCaptcha(LETTER_CHARS, length);
    }

    /**
     * 生成数字字母混合验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateMixedCaptcha(int length) {
        return generateCaptcha(MIXED_CHARS, length);
    }

    /**
     * 生成算术验证码
     * @return 包含算式和结果的数组 [0]=算式, [1]=结果
     */
    public static String[] generateMathCaptcha() {
        Random random = new Random();
        int a = random.nextInt(10) + 1;
        int b = random.nextInt(10) + 1;
        int operator = random.nextInt(3); // 0:+, 1:-, 2:*

        String question;
        int result;

        switch (operator) {
            case 0:
                question = a + " + " + b + " = ?";
                result = a + b;
                break;
            case 1:
                // 确保减法结果为正数
                if (a < b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                question = a + " - " + b + " = ?";
                result = a - b;
                break;
            case 2:
                question = a + " × " + b + " = ?";
                result = a * b;
                break;
            default:
                question = a + " + " + b + " = ?";
                result = a + b;
        }

        return new String[]{question, String.valueOf(result)};
    }

    private static String generateCaptcha(String source, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("验证码长度必须大于0");
        }

        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            captcha.append(source.charAt(index));
        }

        return captcha.toString();
    }

    /**
     * 生成带干扰线的验证码图片（需要AWT支持）
     * @param captcha 验证码文本
     * @param width 图片宽度
     * @param height 图片高度
     * @return BufferedImage对象
     */
    public static java.awt.image.BufferedImage generateImageCaptcha(String captcha, int width, int height) {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));

        // 绘制干扰线
        Random random = new Random();
        g.setColor(java.awt.Color.LIGHT_GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码字符
        for (int i = 0; i < captcha.length(); i++) {
            g.setColor(new java.awt.Color(
                    random.nextInt(150) + 50,
                    random.nextInt(150) + 50,
                    random.nextInt(150) + 50));
            g.drawString(String.valueOf(captcha.charAt(i)), 20 + i * 25, 30);
        }

        g.dispose();
        return image;
    }
}
