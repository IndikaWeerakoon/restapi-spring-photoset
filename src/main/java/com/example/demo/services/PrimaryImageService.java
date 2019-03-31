package com.example.demo.services;

import com.example.demo.model.PrimaryImage;
import com.example.demo.repository.PrimaryImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface PrimaryImageService {
    public ResponseEntity PrimaryImageUpload(MultipartFile file);
}
