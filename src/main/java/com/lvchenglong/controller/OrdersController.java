package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Orders;
import com.lvchenglong.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 下单（增加）
     * @param orders(goodsId、num、type、groupOrderId)
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Orders orders){
        Orders savedOrder = ordersService.add(orders);
        return Result.success(savedOrder);
    }

    /**
     * 秒杀下单
     * 高并发：同一时间  出现大量的请求
     * 00:00 这一刻 数以万计的请求会打过来  高并发的解决方式就是限流
     * @param orders
     * @return
     */
    @PostMapping("/addFlashOrder")
    public Result addFlashOrder(@RequestBody Orders orders){
        Orders savedOrder = ordersService.addFlashOrder(orders);
        return Result.success(savedOrder);
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        ordersService.delete(id);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        ordersService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Orders orders = ordersService.selectById(id);
        return Result.success(orders);
    }
    // @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Orders orders){
        List<Orders> list = ordersService.selectAll(orders);
        return Result.success(list);
    }

    @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Orders orders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Orders> pageInfo = ordersService.selectPage(orders,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
