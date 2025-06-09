package com.linyelai.service.impl;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private String bucketName;

    // 检查存储桶是否存在
    public boolean bucketExists() throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    // 创建存储桶
    public void makeBucket() throws Exception {
        if (!bucketExists()) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    // 上传文件
    public String uploadFile(MultipartFile file, String objectName) throws Exception {
        // 如果objectName为空，使用文件原名
        if (objectName == null || objectName.isEmpty()) {
            objectName = file.getOriginalFilename();
        }

        // 确保存储桶存在
        makeBucket();

        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        return objectName;
    }

    // 下载文件
    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    // 获取文件URL
    public String getFileUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS)  // 7天有效期
                        .build());
    }

    // 删除文件
    public void removeFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    // 列出所有文件
    public List<String> listFiles() throws Exception {
        List<String> list = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());

        for (Result<Item> result : results) {
            Item item = result.get();
            list.add(item.objectName());
        }
        return list;
    }
}
