package com.lvchenglong.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.entity.Category;
import com.lvchenglong.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;


    public void add(Category category) {
        categoryMapper.insert(category);
    }


    public void delete(Integer id) {
        categoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Category category) {
        categoryMapper.updateById(category);
    }

    public Category selectById(Integer id) {
        Category category = categoryMapper.selectById(id);
        return category;
    }

    public List<Category> selectAll(Category category) {
        List<Category> list = categoryMapper.selectAll(category);
        return list;
    }


    public PageInfo<Category> selectPage(Category category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }


}
