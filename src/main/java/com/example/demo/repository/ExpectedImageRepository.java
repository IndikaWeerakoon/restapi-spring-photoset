package com.example.demo.repository;

import com.example.demo.model.ExpectedImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpectedImageRepository extends CrudRepository<ExpectedImage,Long> {
    @Override
    Optional<ExpectedImage> findById(Long aLong);


}
