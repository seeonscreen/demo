package com.example.demo;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller

public class restController {
    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private ArrestsRepository arrestsRepository;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("ResultCode","5");
        return new ResponseEntity(jo.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Client> validateData(ClientRequest request) throws JSONException {

        String strForResultText = "";
        JSONObject jo = new JSONObject();
        String regexForDul = "";

        if (request.getTypeDUL() != 1 && request.getTypeDUL() != 2) {
            strForResultText += "Неверный  Внутренний код (Тип ДУЛ)";
        }
        if (request.getOrganCode() != 39 && request.getOrganCode() != 17) {
            strForResultText += "Неверный  Код Государственного органа";
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

       /* Pattern patternForLastNameAndFirstName = Pattern.compile("^[а-яА-Я]{1,100}$");
        Matcher matcherForLastName = patternForLastNameAndFirstName.matcher(request.getLastName());
        Matcher matcherForFirstName = patternForLastNameAndFirstName.matcher(request.getFirstName());

        Pattern patternForPlaceOfBirth = Pattern.compile("^.{1,250}$");
        Matcher matcherForPlaceOfBirth = patternForPlaceOfBirth.matcher(request.getPlaceOfBirth());

        Pattern patternForDocNum = Pattern.compile("^[\\dа-яА-Я \\-#№a-zA-Z]{1,30}$");
        Matcher matcherForDocNum = patternForDocNum.matcher(request.getDocNum());

        Pattern patternForPurpose = Pattern.compile("^.{1,1000}$");
        Matcher matcherForPurpose = patternForPurpose.matcher(request.getPurpose());*/


        if (!matcherForDul.matches()) {
            strForResultText += "Неверный формат ввода номера и серии паспорта. ";
        }
       /* if (!matcherForDocNum.matches()) {
            strForResultText += "Неверно введены данные в поле 'Номер первичного документа' (строка до 30 символов, допускаются символы латиницы, кириллицы, цифры и символы «#»,»№», «-»). ";
        }
        if (!matcherForPurpose.matches()) {
            strForResultText += "Кол-во символов в поле 'Основание' не должно превышать 1000. ";
        }
        */


        if (strForResultText != "") {
            jo.put("ResultCode", "3");
            jo.put("ResultText", strForResultText);
        }
        if (jo.length() != 0)
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        else return null;
    }

    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> api(@Valid @RequestBody ClientRequest request, BindingResult result) throws JSONException {

         if(result.hasErrors())
        {
            String er = result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            return  new ResponseEntity (er,HttpStatus.OK);
        }

        JSONObject jo = new JSONObject();

        ResponseEntity answer = validateData(request);
        if (answer != null) {
            return answer;
        }
        Client clients = clientsRepository.findByLastNameAndFirstNameAndTypeDULAndNumbSeries(request.getLastName(), request.getFirstName(), request.getTypeDUL(), request.getNumbSeries());
        if (clients == null) {
            Client newClient = new Client(request.getFirstName(), request.getLastName(), request.getTypeDUL(), request.getNumbSeries(), request.getBirthDate(), request.getPlaceOfBirth());
            clientsRepository.save(newClient);
            clients = clientsRepository.findByLastNameAndFirstNameAndTypeDULAndNumbSeries(request.getLastName(), request.getFirstName(), request.getTypeDUL(), request.getNumbSeries());
        }
        Arrests arrests = arrestsRepository.findByDocNum(request.getDocNum());

        if (request.getOperation() == 1 && arrests == null) {
            Arrests newArrests = new Arrests(request.getOrganCode(), request.getDocDate(), request.getDocNum(), request.getPurpose(), request.getAmount(), request.getOperation(), "Действующий", clients);
            arrestsRepository.save(newArrests);
            jo.put("ArrestId", arrests.getId().toString());
            jo.put("ResultCode", "0");
        } else if (request.getOperation() == 2 && arrests != null) {
            String status = "";
            if (request.getAmount() > 0)
                status = "Действующий";
            else
                status = "Отменен";
            Arrests newArrests = new Arrests(arrests.getId(), request.getOrganCode(), request.getDocDate(), request.getDocNum(), request.getPurpose(), request.getAmount(), request.getOperation(), status, clients);
            arrestsRepository.save(newArrests);
            jo.put("ArrestId", arrests.getId().toString());
            jo.put("ResultCode", "0");
        } else if (request.getOperation() == 3 && arrests != null) {
            Arrests newArrests = new Arrests(arrests.getId(), request.getOrganCode(), request.getDocDate(), request.getDocNum(), request.getPurpose(), request.getAmount(), request.getOperation(), "Отменен", clients);
            arrestsRepository.save(newArrests);
            jo.put("ArrestId", arrests.getId().toString());
            jo.put("ResultCode", "0");
        }
        return new ResponseEntity(jo.toString(), HttpStatus.OK);
    }

}


