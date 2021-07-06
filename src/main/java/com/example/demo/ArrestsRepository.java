package com.example.demo;

import org.springframework.data.repository.CrudRepository;


public interface ArrestsRepository extends CrudRepository<Arrests,Long> {

    Arrests findByDocNum(String docNum );

}