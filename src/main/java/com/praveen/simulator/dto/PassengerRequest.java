package com.praveen.simulator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequest {
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String country;

    public PassengerRequest(String[] csvRow) {
        this.fullName = csvRow[1].trim();
        this.firstName = csvRow[2].trim();
        this.lastName = csvRow[3].trim();
        this.email = csvRow[4].trim();
        this.phone = csvRow[5].trim();
        this.city = csvRow[6].trim();
        this.country = csvRow[7].trim();
    }

}
