package com.lvchenglong.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.common.Constant;
import com.lvchenglong.common.enums.LogsModuleEnum;
import com.lvchenglong.common.enums.RoleEnum;
import com.lvchenglong.common.system.AsyncTaskFactory;
import com.lvchenglong.entity.User;
import com.lvchenglong.exception.CustomException;
import com.lvchenglong.mapper.UserMapper;
import com.lvchenglong.utils.RedisUtils;
import com.lvchenglong.utils.SaUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public User login(User user) {
        String uuid = user.getUuid();
        String captchaKey = Constant.REDIS_KEY_CAPTCHA + uuid;
        String captchaCode = RedisUtils.getCacheObject(captchaKey);
        if (captchaCode == null){
            throw new CustomException("验证码已失效");
        }
        if(!user.getCode().equals(captchaCode)) {
            throw new CustomException("验证码错误");
        }
        // 验证完成后删除缓存
        RedisUtils.deleteObject(captchaKey);

        String username = user.getUsername();
        User dbUser = userMapper.selectByUserName(username);
        if(dbUser == null){
            throw new CustomException("账号不存在");
        }
        String md5Password = securePassword(user.getPassword());
        if(!dbUser.getPassword().equals(md5Password)){
            throw new CustomException("密码错误");
        }
        // 返回token
        StpUtil.login(dbUser.getId());
        String token = StpUtil.getTokenValue();
        dbUser.setToken(token);

        System.out.println("============login 开始执行异步任务============");
        // 异步日志记录
        AsyncTaskFactory.recordLog(LogsModuleEnum.USER.value, "登录", dbUser.getId());
        System.out.println("============login 异步任务执行结束============");

        return dbUser;
    }

    public void register(User user) {
        user.setRole(RoleEnum.USER.name());
        this.add(user);
        // 异步日志记录
        AsyncTaskFactory.recordLog(LogsModuleEnum.USER.value, "注册", user.getId());
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
        // 获取当前登录用户，如果角色是用户就校验原密码
        User loginUser = SaUtils.getLoginUser();
        if(RoleEnum.USER.name().equals(loginUser.getRole())){
            String md5Password = securePassword(user.getPassword());
            if(!dbUser.getPassword().equals(md5Password)){
                throw new CustomException("你输入的密码与原密码不一致");
            }
        }
        // 加密新密码
        user.setNewPassword(securePassword(user.getNewPassword()));
        // 异步日志记录
        AsyncTaskFactory.recordLog(LogsModuleEnum.USER.value, "修改密码", loginUser.getId());
        // 修改密码
        userMapper.updatePassword(user);
    }

}
