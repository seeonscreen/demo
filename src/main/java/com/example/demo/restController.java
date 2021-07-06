package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller

public class restController {
    public static final String status_Acting ="Действующий";
    public static final String status_Canceled="Отменен";
    public static final String status_Executed ="Исполненный";

    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private ArrestsRepository arrestsRepository;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("ResultCode", "5");
        return new ResponseEntity(jo.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String validateData(ClientRequest request) throws JSONException {

        String strForResultText = "";
        String regexForDul = "";

        if (request.getTypeDUL() != 1 && request.getTypeDUL() != 2) {
            strForResultText += "Неверный  Внутренний код (Тип ДУЛ). ";
        }
        if (request.getOrganCode() != 39 && request.getOrganCode() != 17) {
            strForResultText += "Неверный  Код Государственного органа. ";
        }
        if (request.getOrganCode() == 39) {
            if (request.getTypeDUL() == 1) {
                regexForDul = "\\d{2}\\s\\d{2}\\s\\d{6}";
            } else if (request.getTypeDUL() == 2) {
                regexForDul = "\\d{2}\\s\\d{6}";
            }
        } else if (request.getOrganCode() == 17) {
            if (request.getTypeDUL() == 1) {
                regexForDul = "\\d{6}\\-\\d{4}";
            } else if (request.getTypeDUL() == 2) {
                regexForDul = "\\d{2}\\.\\d{6}";
            }
        }
        Pattern patternForDul = Pattern.compile(regexForDul);
        Matcher matcherForDul = patternForDul.matcher(request.getNumbSeries());

        if (!matcherForDul.matches()) {
            strForResultText += "Неверный формат ввода номера и серии паспорта. ";
        }
        if (strForResultText != "") {
            return strForResultText;
        } else return null;
    }

    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity api(@Valid @RequestBody ClientRequest request, BindingResult result) throws JSONException {

        JSONObject jo = new JSONObject();
        String answerAboutValidate = validateData(request);
        String errors = "";
        jo.put("ResultCode", "3");
        if (answerAboutValidate != null || result.hasErrors()) {
            if (result.hasErrors()) {
                errors = result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" "));
            }
            if (answerAboutValidate != null) {
                errors += answerAboutValidate;
            }
            jo.put("ResultText", errors);
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        Client clients = clientsRepository.findByLastNameAndFirstNameAndTypeDULAndNumbSeries(request.getLastName(), request.getFirstName(), request.getTypeDUL(), request.getNumbSeries());
        if (clients == null) {
            Client newClient = new Client(request.getFirstName(), request.getLastName(), request.getTypeDUL(), request.getNumbSeries(), request.getBirthDate(), request.getPlaceOfBirth());
            clientsRepository.save(newClient);
            clients = clientsRepository.findByLastNameAndFirstNameAndTypeDULAndNumbSeries(request.getLastName(), request.getFirstName(), request.getTypeDUL(), request.getNumbSeries());
        }
        Arrests arrests = arrestsRepository.findByDocNum(request.getDocNum());
        String status = "";
        if (request.getOperation() == 1 && arrests == null) {
            status = status_Acting;
            arrests = new Arrests(request.getOrganCode(), request.getDocDate(), request.getDocNum(), request.getPurpose(), request.getAmount(), request.getOperation(), status, clients);
            arrestsRepository.save(arrests);
            jo.put("ArrestId", arrests.getId().toString());
            jo.put("ResultCode", "0");
        }else if(request.getOperation() == 1 && arrests != null)
        {
            jo.put("ResultText", "Данный арест уже существует");
        }
        else if (request.getOperation() == 2 && arrests != null) {

            if (request.getAmount() > 0)
                status = status_Acting;
            else
                status = status_Canceled;
            createNewArrest(request, jo, clients, arrests, status);
        } else if (request.getOperation() == 3 && arrests != null) {
            status=status_Canceled;
            createNewArrest(request, jo, clients, arrests, status);
        }
        return new ResponseEntity(jo.toString(), HttpStatus.OK);
    }
    private void createNewArrest( ClientRequest request, JSONObject jo, Client clients, Arrests arrests,String status) throws JSONException {
        Arrests newArrests = new Arrests(arrests.getId(), request.getOrganCode(), request.getDocDate(), request.getDocNum(), request.getPurpose(), request.getAmount(), request.getOperation(), status, clients);
        arrestsRepository.save(newArrests);
        jo.put("ArrestId", arrests.getId().toString());
        jo.put("ResultCode", "0");
    }
}


