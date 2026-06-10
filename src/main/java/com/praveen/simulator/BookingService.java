//package com.praveen.simulator;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookingService {
//    private List<FlightDetail> flights = new ArrayList<>();
//    private List<PassengerRequest> passengers = new ArrayList<>();
//    private List<APP> activeBookings = new ArrayList<>();
//
//    // 1. Ingest structural route data and generate timelines
//    public void loadFlights(String path) throws IOException {
//        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//            String line;
//            boolean isHeader = true;
//            while ((line = br.readLine()) != null) {
//                if (isHeader) { isHeader = false; continue; }
//                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                flights.add(new FlightDetail(cols));
//            }
//        }
//    }
//
//    // 2. Ingest your passenger CSV rows
//    public void loadPassengers(String path) throws IOException {
//        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//            String line;
//            boolean isHeader = true;
//            while ((line = br.readLine()) != null) {
//                if (isHeader) { isHeader = false; continue; }
//                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                passengers.add(new PassengerRequest(cols));
//            }
//        }
//    }
//
//    // 3. Process dynamic PNR assignments
//    public void createTestBookings() {
//        System.out.println("\n⚡ Processing system checkout & PNR initialization...");
//
//        // Loop passengers and attempt to match them dynamically to an international flight segment
//        for (PassengerRequest passenger : passengers) {
//            if (!flights.isEmpty()) {
//                // For demonstration, map passengers sequentially to available departures
//                FlightDetail targetFlight = flights.get(activeBookings.size() % flights.size());
//
//                APP booking = new APP(passenger, targetFlight);
//                activeBookings.add(booking);
//            }
//        }
//    }
//
//    public void displayManifest() {
//        activeBookings.forEach(System.out::println);
//    }
//
//   public void displayPNR() {
//       System.out.println(activeBookings.stream().findFirst().get().toEdifact());
//    }
//
//}