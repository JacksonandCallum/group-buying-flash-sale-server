package com.lvchenglong.mapper;

import com.lvchenglong.entity.User;

public interface UserMapper {
    User selectByUserName(String username);
}
