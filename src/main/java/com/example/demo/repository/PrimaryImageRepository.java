package com.example.demo.repository;

import com.example.demo.model.PrimaryImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrimaryImageRepository extends CrudRepository<PrimaryImage,Long> {

    @Query("SELECT MAX(p.id) FROM PrimaryImage p WHERE p.imageName = :imageName")
    Optional<Long> findByImageName(@Param("imageName") String imageName);
}
