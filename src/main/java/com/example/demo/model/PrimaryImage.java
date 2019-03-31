package com.example.demo.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public PrimaryImage(String imageName, String imageUrl, boolean masked){
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.masked = masked;
    }

    public PrimaryImage(){}
}
