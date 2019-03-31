package com.example.demo.services;

import com.google.cloud.storage.Bucket;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoogleCloudStorage {
    void createBucket(String name);
    Bucket getBucket();
    List<String> uploadFile(MultipartFile filePart, final String bucketName) throws IOException;
}
