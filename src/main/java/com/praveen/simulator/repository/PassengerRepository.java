package com.praveen.simulator.repository;

import com.praveen.simulator.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Integer> {
    boolean existsByDocumentDetails_PassportNumber(String passportNumber);
    boolean existsByEmail(String emailId);

}
