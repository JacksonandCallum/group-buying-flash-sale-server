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
}
