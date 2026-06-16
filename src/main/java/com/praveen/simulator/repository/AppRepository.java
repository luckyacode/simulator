package com.praveen.simulator.repository;

import com.praveen.simulator.entity.APP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<APP,Integer> {
    Optional<APP> findByAppId(String appId);
    Optional<APP> findById(int appId);
    Optional<APP> findByPnrId(String pnrId);
    Optional<APP> findByGovernmentClearanceResponse_ClearanceId(String clearanceId);
    Optional<APP> findByGovernmentClearanceResponse_PassengerId(String passengerId);

}
