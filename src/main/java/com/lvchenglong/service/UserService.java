package com.lvchenglong.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Constant;
import com.lvchenglong.entity.User;
import com.lvchenglong.exception.CustomException;
import com.lvchenglong.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
        String md5Password = securePassword(user.getPassword());
        if(!dbUser.getPassword().equals(md5Password)){
            throw new CustomException("密码错误");
        }
        return dbUser;
    }

    public void add(User user) {
        if(ObjectUtil.isEmpty(user.getUsername())){
            throw new CustomException("账号不能为空");
        }
        if(ObjectUtil.isEmpty(user.getRole())){
            throw new CustomException("角色不能为空");
        }
        User dbUser = userMapper.selectByUserName(user.getUsername());
        if(dbUser != null){
            throw new CustomException("账号已存在");
        }
        if(ObjectUtil.isEmpty(user.getPassword())){
            throw new CustomException("密码不能为空");
        }else{
            user.setPassword(securePassword(user.getPassword()));
        }
        if(ObjectUtil.isEmpty(user.getName())){
            user.setName(user.getUsername());
        }
        userMapper.insert(user);
    }

    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public User selectById(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    public List<User> selectAll(User user) {
        List<User> list = userMapper.selectAll(user);
        return list;
    }


    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public String securePassword(String password){
        return SecureUtil.md5(password + Constant.PASSWORD_SALT);
    }

    public void updatePassword(User user) {
        User dbUser = userMapper.selectByUserName(user.getUsername());
        if(ObjectUtil.isEmpty(dbUser)){
            throw new CustomException("账户不存在");
        }
        String md5Password = securePassword(user.getPassword());
        if(!dbUser.getPassword().equals(md5Password)){
            throw new CustomException("你输入的密码与原密码不一致");
        }
        // 加密新密码
        user.setNewPassword(securePassword(user.getNewPassword()));
        // 修改密码
        userMapper.updatePassword(user);
    }
}
