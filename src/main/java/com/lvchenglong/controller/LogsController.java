package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Logs;
import com.lvchenglong.service.LogsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogsController {
    @Resource
    LogsService logsService;

    @SaCheckRole("ADMIN")
    @PostMapping("/add")
    public Result add(@RequestBody Logs logs){
        logsService.add(logs);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        logsService.delete(id);
        return Result.success();
    }
    @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        logsService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Logs logs){
        logsService.updateById(logs);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Logs logs = logsService.selectById(id);
        return Result.success(logs);
    }
    @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Logs logs){
        List<Logs> list = logsService.selectAll(logs);
        return Result.success(list);
    }

    @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Logs logs,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Logs> pageInfo = logsService.selectPage(logs,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
