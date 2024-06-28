package com.lvchenglong.mapper;

import com.lvchenglong.entity.User;

import java.util.List;

public interface UserMapper {
    User selectByUserName(String username);

    void insert(User user);

    void deleteById(Integer id);

    void updateById(User user);


    List<User> selectAll(User user);

    User selectById(Integer id);
}
