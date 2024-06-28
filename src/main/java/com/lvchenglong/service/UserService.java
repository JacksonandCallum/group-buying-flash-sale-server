package com.lvchenglong.service;

import cn.hutool.crypto.SecureUtil;
import com.lvchenglong.common.Constant;
import com.lvchenglong.entity.User;
import com.lvchenglong.exception.CustomException;
import com.lvchenglong.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public User login(User user) {
        String username = user.getUsername();
        User dbUser = userMapper.selectByUserName(username);
        if(dbUser == null){
            throw new CustomException("账号不存在");
        }
        String md5Password = SecureUtil.md5(user.getPassword() + Constant.PASSWORD_SALT);
        if(!dbUser.getPassword().equals(md5Password)){
            throw new CustomException("密码错误");
        }
        return dbUser;
    }
}
