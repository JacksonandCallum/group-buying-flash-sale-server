package com.lvchenglong.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Goods {
    private Integer id;
    private String name;
    private BigDecimal originPrice;
    private Boolean hasGroup;
    private BigDecimal groupPrice;
    private Boolean hasFlash;
    private BigDecimal flashPrice;
    private String content;
    private String img;
    private Integer categoryId;
    private String date;
    private Integer store;
    private String flashTime;
    private Integer flashNum;

    private String categoryName;
}
