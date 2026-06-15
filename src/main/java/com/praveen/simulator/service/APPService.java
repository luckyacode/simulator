package com.praveen.simulator.service;

import com.praveen.simulator.entity.APP;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.repository.APPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class APPService {
    private final APPRepository appRepository;


    public List<APP> getAllAPPData() {
        return appRepository.findAll();
    }

    public Optional<APP> getAppDataById(int id) {
        return appRepository.findByAppId(id);
    }

    public Optional<APP> getAppDataByPnrId(String pnrId) {
        return appRepository.findByPnrId(pnrId);
    }

    public Optional<APP> searchAppData(String clearanceId, String passengerId) {
        if(clearanceId!=null)
            return appRepository.findByGovernmentClearanceResponse_ClearanceId(clearanceId);
        return appRepository.findByGovernmentClearanceResponse_PassengerId(passengerId);
    }

}
