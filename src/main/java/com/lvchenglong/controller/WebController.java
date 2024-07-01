package com.lvchenglong.controller;

import com.lvchenglong.common.Result;
import com.lvchenglong.entity.User;
import com.lvchenglong.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/web")
@RestController
public class WebController {
    @Resource
    private UserService userService;

    @GetMapping
    public Result CeShi(){
        String data = "hello world";
        return Result.success(data);
    }

    /**
     * 登录接口
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User dbUser = userService.login(user);
        return Result.success(dbUser);
    }

    /**
     * 注册接口
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return Result.success();
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user){
        userService.updatePassword(user);
        return Result.success();
    }
}
