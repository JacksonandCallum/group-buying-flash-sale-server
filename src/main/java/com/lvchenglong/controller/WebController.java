package com.lvchenglong.controller;

import com.lvchenglong.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/web")
@RestController
public class WebController {
    @GetMapping
    public Result CeShi(){
        String data = "hello world";
        return Result.success(data);
    }
}
