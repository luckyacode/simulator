package com.praveen.simulator.controller;

import com.praveen.simulator.entity.APP;
import com.praveen.simulator.helper.AirlineException;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.repository.APPRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/APP")
@RequiredArgsConstructor
public class APPController {
    private final APPRepository appRepository;

    @GetMapping("/getById/{id}")
    public APP getById(@PathVariable int id) throws Exception {
       return  appRepository.findById(id).orElseThrow(()-> AirlineException.badRequest("app data not present"));
    }

    @GetMapping("/getByPNRId/{id}")
    public APP getByPNRId(@PathVariable int id) throws Exception {
       return  appRepository.findById(id).orElseThrow(()->AirlineException.badRequest("app data not present"));
    }

    @GetMapping("/getAPPMessageById/{id}")
    public String getAPPMessageById(@PathVariable String id) throws Exception {
        return Utils.convertToAppEdifact(appRepository.findByAppId(id).orElseThrow(()-> AirlineException.badRequest("app not found")));
    }

    @GetMapping("/getAll")
    public List<APP> getAll()  {
       return  appRepository.findAll();
    }

    @SneakyThrows
    @GetMapping("/appByClearanceId/{clearanceId}")
    public APP appByClearanceId(@PathVariable String clearanceId){
        return appRepository.findByGovernmentClearanceResponse_ClearanceId(clearanceId).orElseThrow(()->AirlineException.badRequest("app data not present"));
    }

    @SneakyThrows
    @GetMapping("/appByPassengerId/{passengerId}")
    public APP appByPassengerId(@PathVariable String passengerId){
        return appRepository.findByGovernmentClearanceResponse_PassengerId(passengerId).orElseThrow(()->AirlineException.badRequest("app data not present"));
    }


}
