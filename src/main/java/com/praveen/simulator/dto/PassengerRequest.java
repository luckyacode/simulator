package com.praveen.simulator.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class PassengerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String country;
    private DocumentDetails documentDetails;

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
