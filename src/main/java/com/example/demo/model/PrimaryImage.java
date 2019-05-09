package com.example.demo.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Data
public class PrimaryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter@Getter
    private String imageName;
    @Setter@Getter
    private String imageUrl;
    @Setter@Getter
    private boolean masked;
    @Setter@Getter
    private String uploadTime;

    public PrimaryImage(String imageName, String imageUrl, boolean masked,String uploadTime){
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.masked = masked;
        this.uploadTime = uploadTime;
    }

    public PrimaryImage(){}
}
