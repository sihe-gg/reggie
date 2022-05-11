package com.test.controller.backend;

import com.test.common.Result;
import com.test.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 图片上传下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie-test-path}")
    private String testPath;

    @Value("${reggie-product-path}")
    private String productPath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        // 判断是 win 还是 linux
        String rootPath = System.getProperty("os.name").toLowerCase().startsWith("win") ? testPath : productPath;

        // 获取项目根路径
        log.info("图片上传的根目录为：{}", rootPath);

        // 获取随机名称和后缀
        String suffix = file.getOriginalFilename();
        suffix = suffix.substring(suffix.lastIndexOf("."));
        String originalName = UUID.randomUUID().toString() + suffix;

        try {
            // 转存图片到指定位置
            file.transferTo(new File(rootPath + originalName));

        } catch (IOException e) {
            throw new CustomException("图片上传失败，请重试!");
        }

        return Result.success(originalName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        // 判断是 win 还是 linux
        String rootPath = System.getProperty("os.name").toLowerCase().startsWith("win") ? testPath : productPath;

        // 输入流读取图片
        try {
            FileInputStream fis = new FileInputStream(new File(rootPath + name));
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fis.read(bytes)) != -1) {
                // 通过输出流发送图片
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
