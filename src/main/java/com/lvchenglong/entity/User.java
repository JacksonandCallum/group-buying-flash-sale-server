package com.lvchenglong.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String avatar;
    private String phone;
    private String email;
    private String newPassword;
    private String token;
    // 验证码的key
    private String uuid;
    // 用户填写的验证码
    private String code;
}
