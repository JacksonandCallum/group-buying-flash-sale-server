package com.lvchenglong.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.Comment;
import com.lvchenglong.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentService commentService;

    // @SaCheckRole("ADMIN")
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment){
        commentService.add(comment);
        return Result.success();
    }

    // @SaCheckRole("ADMIN")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        commentService.delete(id);
        return Result.success();
    }

    // @SaCheckRole("ADMIN")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        commentService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Comment comment){
        commentService.updateById(comment);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Comment comment = commentService.selectById(id);
        return Result.success(comment);
    }

    // @SaCheckRole("ADMIN")
    @GetMapping("/selectAll")
    public Result selectAll(Comment comment){
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }

    // @SaCheckRole("ADMIN")
    @GetMapping("/selectPage")
    public Result selectPage(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Comment> pageInfo = commentService.selectPage(comment,pageNum,pageSize);
        return Result.success(pageInfo);
    }
}
