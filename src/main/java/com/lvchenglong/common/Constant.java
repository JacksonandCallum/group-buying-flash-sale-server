package com.lvchenglong.common;

public interface Constant {
    // 密码盐
    String PASSWORD_SALT = "9737fad12ef05621a2e0bd2aaeb018e5";

    // redis key
    String REDIS_KEY_CAPTCHA = "captcha_code:";

    // 验证码过期的时间。单位：分钟
    int CAPTCHA_EXPIRE_MINUTES = 1;
}
