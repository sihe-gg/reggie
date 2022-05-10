package com.test.utils;

import com.test.exception.CustomException;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
public class ValidateCodeUtils {

    /**
     * 随机生成验证码
     * @param length
     * @return
     */
    public static Integer generateValidateCode(int length) {
        Integer code = null;
        if(length == 4) {
            code = new Random().nextInt(9999);// 生成随机数，最大为 9999
            if(code < 1000) {
                code = code + 1000; // 保证验证码 4 位
            }
        }else if(length == 6) {
            code = new Random().nextInt(999999);// 生成随机数，最大为 999999
            if(code < 100000) {
                code = code + 100000; // 保证为 4 位
            }
        }else {
            throw new CustomException("只能生成 4 位或者 6 位验证码！");
        }

        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length
     * @return
     */
    public static String generateValidateCode4String(int length) {
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);

        return capstr;
    }
}
