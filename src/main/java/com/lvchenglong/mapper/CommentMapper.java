package com.lvchenglong.mapper;

import com.lvchenglong.entity.Comment;

import java.util.List;

public interface CommentMapper {
    void insert(Comment comment);

    void deleteById(Integer id);

    void updateById(Comment comment);

    List<Comment> selectAll(Comment comment);

    Comment selectById(Integer id);
}
