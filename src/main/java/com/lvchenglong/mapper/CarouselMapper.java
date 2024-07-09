package com.lvchenglong.mapper;

import com.lvchenglong.entity.Carousel;

import java.util.List;

public interface CarouselMapper {
    void insert(Carousel carousel);

    void deleteById(Integer id);

    void updateById(Carousel carousel);

    List<Carousel> selectAll(Carousel carousel);

    Carousel selectById(Integer id);
}
