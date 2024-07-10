package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Goods;
import com.lvchenglong.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    GoodsService goodsService;
    @SaCheckRole("ADMIN")
    @PostMapping("/add")
    public Result add(@RequestBody Goods goods){
        goodsService.add(goods);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        goodsService.delete(id);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        goodsService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Goods goods){
        goodsService.updateById(goods);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Goods goods = goodsService.selectById(id);
        return Result.success(goods);
    }
    // @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Goods goods){
        List<Goods> list = goodsService.selectAll(goods);
        return Result.success(list);
    }

    @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Goods> pageInfo = goodsService.selectPage(goods,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 查询秒杀商品（限量2个）
     * @param goods
     * @return
     */
    @GetMapping("/selectFlash")
    public Result selectFlash(Goods goods){
        List<Goods> list = goodsService.selectFlash(goods);
        return Result.success(list);
    }
}
