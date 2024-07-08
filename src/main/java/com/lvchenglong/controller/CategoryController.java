package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Category;
import com.lvchenglong.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;
    @SaCheckRole("ADMIN")
    @PostMapping("/add")
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.delete(id);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        categoryService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Category category = categoryService.selectById(id);
        return Result.success(category);
    }
    @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Category category){
        List<Category> list = categoryService.selectAll(category);
        return Result.success(list);
    }

    @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Category category,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Category> pageInfo = categoryService.selectPage(category,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
