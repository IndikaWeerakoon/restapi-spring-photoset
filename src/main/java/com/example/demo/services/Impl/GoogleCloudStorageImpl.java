package com.example.demo.services.Impl;

import com.example.demo.services.GoogleCloudStorage;
import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoogleCloudStorageImpl implements GoogleCloudStorage {
    private final String credentialFile = "PhotoUpload-652efd782715.json";
    private final String project = "photoupload-235503";
    private Bucket bucket;
    private Storage storage;
    private Credentials credentials;

    @Autowired
    public GoogleCloudStorageImpl() throws IOException {
        this.credentials = GoogleCredentials.fromStream(new FileInputStream(credentialFile));
        this.storage = StorageOptions.newBuilder().setCredentials(credentials)
                .setProjectId(this.project).build().getService();
    }

    public void createBucket(String name){
        this.bucket = this.storage.create(BucketInfo.of(name));
    }

    @Override
    public Bucket getBucket() {
        return this.bucket;
    }
    public List<String> uploadFile(MultipartFile filePart, final String bucketName)throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("-YYYY-MM-dd-HHmmssSSS");
        String date = format.format(new Date());
        List<String> databaseDetails = new ArrayList<String>();
        String fileName[] = filePart.getOriginalFilename().split("\\.");
        final String name = fileName[0] + date +"."+ fileName[fileName.length-1];
        databaseDetails.add(name);

        // the inputstream is closed by default, so we don't need to close it here
        BlobInfo blobInfo =
                this.storage.create(
                        BlobInfo
                                .newBuilder(bucketName, name)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(),
                        filePart.getInputStream());
        // return the public download link
        databaseDetails.add(blobInfo.getMediaLink());
        return databaseDetails;
    }

}
