package com.lvchenglong.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.lvchenglong.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileController {

    // 当前项目的根目录
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl}")
    private String fileBaseUrl;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        // 定义文件的唯一标识
        String fileName  = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        // 拼接完整的文件路径
        String realFilePath = filePath + fileName;
        try {
            if(!FileUtil.isDirectory(filePath)){
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(),realFilePath);
        } catch (IOException e) {
            log.error("文件上传错误",e);
        }
        String url = fileBaseUrl + "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response){
        ServletOutputStream os;
        // 设置响应头
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");
        try {
            // 拼接完整的文件路径
            String realFilePath = filePath + fileName;
            // 通过文件的存储路径拿到文件流
            byte[] bytes = FileUtil.readBytes(realFilePath);
            // 写出文件流
            os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("文件下载错误",e);
        }
    }

    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            Thread.sleep(1L);
        } catch (Exception e) {
            log.error("wangeditor文件上传错误", e);
        }
        String http = fileBaseUrl + "/files/download/";
        Map<String, Object> resMap = new HashMap<>();
        // wangEditor上传图片成功后， 需要返回的参数
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
        return resMap;
    }
}
