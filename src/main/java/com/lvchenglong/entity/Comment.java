package com.lvchenglong.entity;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer score;
    private String content;
    private String createTime;
    private Integer goodsId;
    private String reply;

    private String userName;
    private String userAvatar;
    private String goodsName;
}
