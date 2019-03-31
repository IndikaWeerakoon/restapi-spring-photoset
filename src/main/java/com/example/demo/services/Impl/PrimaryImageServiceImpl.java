package com.example.demo.services.Impl;

import com.example.demo.data.StringRes;
import com.example.demo.model.PrimaryImage;
import com.example.demo.repository.PrimaryImageRepository;
import com.example.demo.services.GoogleCloudStorage;
import com.example.demo.services.PrimaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PrimaryImageServiceImpl implements PrimaryImageService {

    @Autowired
    PrimaryImageRepository piRepo;
    @Autowired
    GoogleCloudStorage gcs;


    @Override
    public ResponseEntity PrimaryImageUpload(MultipartFile file){
//        File newFile = new File(filePath+file.getOriginalFilename());
        List<String> mediaPath = null;

        if(true){
            try{
//                FileOutputStream fileOut = new FileOutputStream(newFile);
//                fileOut.write(file.getBytes());
//                fileOut.close();
                mediaPath = gcs.uploadFile(file, StringRes.nativeBucket);


            }catch (IOException ex){
                ex.printStackTrace();
                return new ResponseEntity<>("{\"ststus\":\"server error\"}", HttpStatus.SERVICE_UNAVAILABLE);
            }catch (MaxUploadSizeExceededException ex){
                ex.printStackTrace();
                return new ResponseEntity<>("{\"ststus\":\"file size too large\"}",HttpStatus.BAD_REQUEST);
            }finally {
                dbWrite(mediaPath.get(1),mediaPath.get(0));
                return new ResponseEntity<>("{\"ststus\":\"file successfully uploaded\"}",HttpStatus.ACCEPTED);
            }
        }else {
            return new ResponseEntity<>("{\"status\":\"file already exist\"}",HttpStatus.CONFLICT);
        }


    }

    private void dbWrite(String filePath,String fileName){
        PrimaryImage primaryImage = new PrimaryImage(fileName,filePath,false);
        piRepo.save(primaryImage);
    }

    private boolean seachFile(String filename){
        Optional<Long> data = piRepo.findByImageName(filename);
        return data.isPresent();
    }
}
