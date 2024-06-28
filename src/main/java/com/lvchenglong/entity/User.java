package com.lvchenglong.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String avatar;
    private String phone;
    private String email;
}
