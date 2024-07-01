package com.lvchenglong.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logs {
    private Integer id;
    private String module;
    private String operate;
    private Integer userId;
    private String ip;
    private String time;
    private String userName;
}
