package com.example.demo;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;


@Entity
@Table(name="CLIENTS")


public class Client {

    //String GUID;
    //Arrests listOfArrests;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long guid;

    @Column(name="FIRSTNAME")
    private String firstName;

    @Column(name="LASTNAME")
    private String lastName;

    @Column(name="TYPEDUL")
    private Integer typeDUL;

    @Column(name="NUMBANDSERIES")
    private String numbSeries;

    @Column(name="BIRTHDATE")
    private Date birthDate;

    @Column(name="PLACEOFBIRTH")
    private String placeOfBirth;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Arrests> arrests;

    public Client() {

    }

    public Long getId() {
        return guid;
    }

    public void setId(Long guid) {
        this.guid = guid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTypeDUL() {
        return typeDUL;
    }

    public void setTypeDUL(Integer typeDUL) {
        this.typeDUL = typeDUL;
    }

    public String getNumbSeries() {
        return numbSeries;
    }

    public void setNumbSeries(String numbAndSeries) {
        this.numbSeries = numbAndSeries;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /*@Override
    public String toString() {
        return String.format(
                "{'id'=%d, 'firstName'='%s', 'lastName'='%s','numbAndSeries'='%s}",
                guid, firstName, lastName,numbAndSeries);
    }*/
     public Client (String firstName, String lastName, Integer typeDUL, String numbAndSeries, Date birthDate, String placeOfBirth)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.typeDUL = typeDUL;
        this.numbSeries = numbAndSeries;
        this.birthDate=birthDate;
        this.placeOfBirth=placeOfBirth;
    }


}

