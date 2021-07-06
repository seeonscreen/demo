package com.example.demo;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "ARRESTS")
public class Arrests {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;

    @Column(name = "organCode")
    private Integer organCode;

    @Column(name = "docDate")
    private Date docDate;

    @Column(name = "docNum")
    private String docNum;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "amount")
    private Long amount;


    @Column(name = "operation")
    private Integer operation;

    @Column(name = "status")
    private String status;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrganCode() {
        return organCode;
    }

    public void setOrganCode(Integer organCode) {
        this.organCode = organCode;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Client getClients() {
        return client;
    }

    public void setClients(Client clients) {
        this.client = clients;
    }

    public Arrests() {

    }

    public Arrests(Long id, Integer organCode, Date docDate, String docNum, String purpose, Long amount, Integer operation, String status, Client clients) {
        this.id = id;
        this.organCode = organCode;
        this.docDate = docDate;
        this.docNum = docNum;
        this.purpose = purpose;
        this.amount = amount;
        this.operation = operation;
        this.status = status;
        this.client = clients;
    }

    public Arrests(Integer organCode, Date docDate, String docNum, String purpose, Long amount, Integer operation, String status, Client clients) {
        this.organCode = organCode;
        this.docDate = docDate;
        this.docNum = docNum;
        this.purpose = purpose;
        this.amount = amount;
        this.operation = operation;
        this.status = status;
        this.client = clients;
    }
}
