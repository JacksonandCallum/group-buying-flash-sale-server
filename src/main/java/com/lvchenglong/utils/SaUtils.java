package com.lvchenglong.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.lvchenglong.entity.User;
import com.lvchenglong.mapper.UserMapper;

/**
 * SaToken工具类
 */
public class SaUtils {
    /**
     * 获取当前登录用户
     * @return
     */
    public static User getLoginUser(){
        Object loginId = StpUtil.getLoginId();
        if(loginId != null){
            Integer userId = Integer.valueOf(loginId.toString());
            UserMapper userMapper = SpringUtils.getBean(UserMapper.class);
            return userMapper.selectById(userId);
        }
        return null;
    }
}
