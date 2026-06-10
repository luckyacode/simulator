package com.praveen.simulator.helper;

import com.praveen.simulator.model.FlightDetail;
import com.praveen.simulator.other.PassengerRequest;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static List<PassengerRequest> mapPassenger(List<String> list){
        return list.stream().map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(PassengerRequest::new).toList();
    }

    public static List<FlightDetail> mapFlight(List<String> list){
        return list.stream().map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(FlightDetail::new).toList();
    }

    @SneakyThrows
    public static List<PassengerRequest> fetchPassengerFromFile(int size){
        Scanner scan = new Scanner(new File("files/people.csv"));
        List<String> list = new ArrayList<>();
        scan.nextLine();
        while(scan.hasNext()){
            if(list.size()==size)
                break;
            list.add(scan.nextLine());
        }
        return mapPassenger(list);
    }

    @SneakyThrows
    public static List<FlightDetail> fetchFlightFromFile(int size){
        Scanner scan = new Scanner(new File("files/international_flight_traffic.csv"));
        List<String> list = new ArrayList<>();
        scan.nextLine();
        while(scan.hasNext()){
            if(list.size()==size)
                break;
            list.add(scan.nextLine());
        }
        return mapFlight(list);
    }

    public static String generatePnrLocator() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return sb.toString();
    }

}
