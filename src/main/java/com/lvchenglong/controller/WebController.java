package com.lvchenglong.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.lvchenglong.common.Constant;
import com.lvchenglong.common.Result;
import com.lvchenglong.entity.User;
import com.lvchenglong.service.UserService;
import com.lvchenglong.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/web")
@RestController
@Slf4j
public class WebController {
    @Resource
    private UserService userService;

    @Resource
    private Producer producer;

    @GetMapping
    public Result CeShi(){
        String data = "hello world";
        return Result.success(data);
    }

    /**
     * 登录接口
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User dbUser = userService.login(user);
        return Result.success(dbUser);
    }

    /**
     * 注册接口
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return Result.success();
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user){
        userService.updatePassword(user);
        return Result.success();
    }

    /**
     * 获取图形算数验证码
     * @return
     */
    @GetMapping("/captcha")
    public Result getCaptCha(){
        // 验证码存储到redis
        String uuid = IdUtil.fastSimpleUUID();
        String captchaKey = Constant.REDIS_KEY_CAPTCHA + uuid;
        // 生成文本  1+1=2  1+1@2
        String captchaText = producer.createText();
        // 解析出类似于1+1的算术题（去掉@）
        String captchaStr = captchaText.substring(0, captchaText.lastIndexOf("@"));
        // 得出算术题结果
        String captchaCode = captchaText.substring(captchaText.lastIndexOf("@") + 1);
        // 将算术结果存储到redis
        RedisUtils.setCacheObject(captchaKey,captchaCode,Constant.CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        // 返回图片的base64编码
        try {
            FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
            BufferedImage image = producer.createImage(captchaStr);
            ImageIO.write(image,"jpg",outputStream);
            Map<String,Object> map = new HashMap<>();
            map.put("uuid",uuid);
            map.put("img", Base64.encode(outputStream.toByteArray()));
            return Result.success(map);
        }catch (Exception e){
            log.error("生成验证码错误",e);
            return Result.error("获取验证码错误");
        }
    }
}
