package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Carousel;
import com.lvchenglong.service.CarouselService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {
    @Resource
    CarouselService carouselService;

    @SaCheckRole("ADMIN")
    @PostMapping("/add")
    public Result add(@RequestBody Carousel carousel){
        carouselService.add(carousel);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        carouselService.delete(id);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        carouselService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Carousel carousel){
        carouselService.updateById(carousel);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Carousel carousel = carouselService.selectById(id);
        return Result.success(carousel);
    }

    // @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Carousel carousel){
        List<Carousel> list = carouselService.selectAll(carousel);
        return Result.success(list);
    }

    @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Carousel carousel,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Carousel> pageInfo = carouselService.selectPage(carousel,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
