package com.lvchenglong.controller;

import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.User;
import com.lvchenglong.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        userService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        User user = userService.selectById(id);
        return Result.success(user);
    }

    @GetMapping("/selectAll")
    public Result selectAll(User user){
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<User> pageInfo = userService.selectPage(user,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
