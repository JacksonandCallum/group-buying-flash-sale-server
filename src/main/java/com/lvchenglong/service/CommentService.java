package com.lvchenglong.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.entity.Comment;
import com.lvchenglong.mapper.CommentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;


    public void add(Comment comment) {
        comment.setCreateTime(DateUtil.now());
        commentMapper.insert(comment);
    }


    public void delete(Integer id) {
        commentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public Comment selectById(Integer id) {
        Comment comment = commentMapper.selectById(id);
        return comment;
    }

    public List<Comment> selectAll(Comment comment) {
        List<Comment> list = commentMapper.selectAll(comment);
        return list;
    }


    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }


}
