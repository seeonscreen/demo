package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArrestsRepository extends CrudRepository<Arrests,Long> {

    Arrests findByDocNum(String docNum );
    Arrests findById(long id);

}