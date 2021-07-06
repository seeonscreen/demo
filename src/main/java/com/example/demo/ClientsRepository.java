package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface ClientsRepository extends CrudRepository<Client, Long> {

    Client findByLastName(String lastName);
    Client findById(long id);
    Client findByLastNameAndFirstNameAndTypeDULAndNumbSeries(String lastName,String firstName,Integer typeDUL,String numbSeries);

}