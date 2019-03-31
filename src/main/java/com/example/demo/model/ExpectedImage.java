package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ExpectedImage {
    @Id@Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter@Setter
    private String imageName;
    @Getter@Setter
    private String imageUrl;
    @Getter@Setter
    private Long primaryUrlId;

}
