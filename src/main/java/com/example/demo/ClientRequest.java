package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;


import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
@Validated
public class ClientRequest {

    @NotBlank(message ="Поле 'Имя' заполнено некорректно.")
    @Pattern(regexp="^[а-яА-Я]{1,100}$",message="Кол-во символов в поле 'Имя' не должно превышать 100.")
    private String firstName;

    @NotBlank(message ="Поле 'Фамилия' заполнено некорректно.")
    @Pattern(regexp="^[а-яА-Я]{1,100}$",message="Кол-во символов в поле 'Фамилия' не должно превышать 100.")
    private String lastName;

    @NotNull(message ="Поле 'Тип ДУЛ' заполнено некорректно.")
    private Integer typeDUL;

    @NotBlank(message ="Поле 'Номер и Серия' заполнено некорректно.")
    private String numbSeries;

    @NotBlank(message ="Поле 'Место рождения' заполнено некорректно.")
    private String placeOfBirth;

    @NotNull(message ="Поле 'Дата рождения' заполнено некорректно.")
    private Date birthDate;

    @NotNull(message ="Поле 'Код Государственно органа' заполнено некорректно.")
    private Integer organCode;

    @NotNull(message ="Поле 'Дата документа' заполнено некорректно.")
    private Date docDate;

    @NotBlank(message ="Поле 'Номер документа' заполнено некорректно.")
    @Pattern(regexp="^[\\dа-яА-Я \\-#№a-zA-Z]{1,30}$",message = "Неверно введены данные в поле 'Номер первичного документа' (строка до 30 символов, допускаются символы латиницы, кириллицы, цифры и символы «#»,»№», «-»). ")
    private String docNum;

    @NotBlank(message ="Поле 'Основание Ареста' заполнено некорректно")
    @Pattern(regexp="^.{1,1000}$",message ="Кол-во символов в поле 'Основание' не должно превышать 1000.")
    private String purpose;

    @NotNull(message ="Поле 'Сумма ареста' заполнено некорректно.")
    private Long amount;

    @NotNull(message ="Поле 'Тип операции' заполнено некорректно.")
    private Integer operation;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getNumbSeries() {
        return numbSeries;
    }

    public void setNumbSeries(String numbSeries) {
        this.numbSeries = numbSeries;
    }

    public Integer getTypeDUL() {
        return typeDUL;
    }

    public void setTypeDUL(Integer typeDUL) {
        this.typeDUL = typeDUL;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
