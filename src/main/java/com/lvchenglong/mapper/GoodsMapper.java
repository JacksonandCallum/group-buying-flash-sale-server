package com.lvchenglong.mapper;

import com.lvchenglong.entity.Goods;

import java.util.List;

public interface GoodsMapper {
    void insert(Goods goods);

    void deleteById(Integer id);

    void updateById(Goods goods);

    List<Goods> selectAll(Goods goods);

    Goods selectById(Integer id);
}
