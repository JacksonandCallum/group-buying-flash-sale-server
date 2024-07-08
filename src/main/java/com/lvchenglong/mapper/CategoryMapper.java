package com.lvchenglong.mapper;

import com.lvchenglong.entity.Category;

import java.util.List;

public interface CategoryMapper {
    void insert(Category category);

    void deleteById(Integer id);

    void updateById(Category category);

    List<Category> selectAll(Category category);

    Category selectById(Integer id);
}
