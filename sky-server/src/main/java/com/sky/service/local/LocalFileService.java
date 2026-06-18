package com.sky.service.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileService {

    @Value("${sky.upload.local-path}")
    private String uploadDir;

    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty() || originalFilename.trim().isEmpty()) {
            throw new IOException("文件名不能为空");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;

        File dest = new File(uploadDir + fileName);
        if (!dest.getParentFile().exists()) {
            boolean created = dest.getParentFile().mkdirs();
            if (!created) {
                throw new IOException("创建上传目录失败：" + uploadDir);
            }
        }

        file.transferTo(dest);
        log.info("文件上传成功：{}", dest.getAbsolutePath());
        return "http://localhost:8080/images/" + fileName;
    }
}
