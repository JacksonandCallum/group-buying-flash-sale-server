package com.lvchenglong.controller;

import cn.hutool.core.io.FileUtil;
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
}
