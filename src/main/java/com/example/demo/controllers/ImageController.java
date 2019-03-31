package com.example.demo.controllers;

import com.example.demo.data.StringRes;
import com.example.demo.model.ExpectedImage;
import com.example.demo.model.PrimaryImage;
import com.example.demo.repository.ExpectedImageRepository;
import com.example.demo.repository.PrimaryImageRepository;
import com.example.demo.services.GoogleCloudStorage;
import com.example.demo.services.PrimaryImageService;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class ImageController {

    @Autowired
    PrimaryImageRepository primImg;
    @Autowired
    PrimaryImageService primaryImageService;
    @Autowired
    ExpectedImageRepository exRepo;
    @Autowired
    GoogleCloudStorage gcs;



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value = "test", produces = "application/json")
    public List<PrimaryImage> testApi(){
        List<PrimaryImage> imageList = new ArrayList<>();
        Iterable<PrimaryImage> primg = primImg.findAll();
        primg.forEach(imageList::add);
        return imageList;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.POST, value = "image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object>uploadImg(@RequestParam("file") MultipartFile file) {

        ResponseEntity  responseEntity = primaryImageService.PrimaryImageUpload(file);
        return responseEntity;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method =RequestMethod.POST,value = "image/multi/upload",produces = "application/json")
    //here this function try to upload two file at the same time and save them to two differnt location
    public ResponseEntity<Object> UploadRequestExpectedPhotoes(@RequestParam("file") MultipartFile file, @RequestParam("expected") MultipartFile expect){
        String fileExtention = file.getOriginalFilename().split("\\.")[1];
        String fileExpectExtension = expect.getOriginalFilename().split("\\.")[1];
        List<String> nativeFileDb = new ArrayList<>();
        List<String> expectedFileDb = new ArrayList<>();

        if((fileExtention.equals("jpeg")||fileExtention.equals("png")|| fileExtention.equals("jpg")||fileExtention.equals("JPEG")||fileExtention.equals("JPG")|| fileExtention.equals("PNG"))&&
                (fileExpectExtension.equals("jpeg")||fileExpectExtension.equals("png")|| fileExpectExtension.equals("jpg")||fileExpectExtension.equals("JPEG")||fileExpectExtension.equals("PNG")|| fileExpectExtension.equals("JPG"))){
            try{
                nativeFileDb = gcs.uploadFile(file,StringRes.nativeBucket);
                expectedFileDb = gcs.uploadFile(expect, StringRes.expectedBucket);
            }catch(IOException ex){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"File Exception",ex);
            }catch (MaxUploadSizeExceededException ex){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"File Size Exceed",ex);
            }finally {

                PrimaryImage pr = new PrimaryImage();

                pr.setImageName(nativeFileDb.get(0));
                pr.setImageUrl(nativeFileDb.get(1));
                pr.setMasked(false);
                this.primImg.save(pr);

                Optional<Long> imgSearch = this.primImg.findByImageName(nativeFileDb.get(0));
                Long id = imgSearch.get();

                ExpectedImage ex = new ExpectedImage();
                ex.setImageName(expectedFileDb.get(0));
                ex.setImageUrl(expectedFileDb.get(1));
                ex.setPrimaryUrlId(id);

                this.exRepo.save(ex);

                return new ResponseEntity<>(("{\"status\":\"successfully file uploaded\"}"),HttpStatus.OK);
            }


        }else{
            return new ResponseEntity<>(("{\"status\":\"file type mismatch\"}"),HttpStatus.BAD_REQUEST);
        }
    }

//    @RequestMapping(method = RequestMethod.GET,value = "store",produces = "application/json")
//    public ResponseEntity storeInCloude(){
//            gcs.createBucket("upload-expected-bucket");
//        return new ResponseEntity<Object> ("{\"success\":\""+gcs.getBucket()+"\"}",HttpStatus.OK);
//    }

}
