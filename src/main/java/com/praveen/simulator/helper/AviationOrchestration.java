package com.praveen.simulator.helper;


import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.KafkaService;
import com.praveen.simulator.service.CheckInResponseService;
import com.praveen.simulator.service.PNRService;
import com.praveen.simulator.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AviationOrchestration {
    private final KafkaService kafkaService;
    private final CheckInResponseService checkInResponseService;
    private final PassengerService passengerService;
    private final PNRService pnrService;

    public PNRRequest createReservation(Passenger passenger, String flightId, double amount) {
        String randomPnr = Utils.generatePnrLocator();
        PNRRequest pnrRequest = PNRRequest.builder().
                PNRId(randomPnr).bookingDateTime(LocalDateTime.now()).bookingStatus("CONFIRMED").flightId(flightId).passengerId(passenger.getId()).ticketStatus(TicketStatus.CONFIRM).bookingChannel(Channel.AIRLINE).bookingClass(BookingClass.J).agencyId("AGENT-01").transactionId(UUID.randomUUID().toString()).totalAmount(amount).currency("INR").build();
        log.info("PNR request created for : {}",pnrRequest);
        log.info("Publishing message for PNRRequest ...");
        kafkaService.sendPNRMessage(pnrRequest);
        return pnrRequest;
    }

    public void performAirportCheckIn(CheckInRequest checkInRequest){
        Passenger passenger = pnrService.getPNRById(checkInRequest.getPnrId()).getPassenger();
        passenger.setDocumentDetails(checkInRequest.getDocumentDetails());
        passengerService.update(passenger);
        log.info("Document updated for passenger : {}",passenger);
        kafkaService.checkInOnAirport(checkInRequest);
    }

    @SneakyThrows
    public CheckInResponse performAirportCheckInResponseByClearance(String clearanceId) {
      return checkInResponseService.getCheckInResponseByClearanceId(clearanceId)
              .orElseThrow(()->AirlineException.badRequest("CheckIn is still in progress...."));
    }

    @SneakyThrows
    public CheckInResponse performAirportCheckInResponseByPassenger(String passengerId) {
      return checkInResponseService.getCheckInResponseByPassengerId(passengerId)
              .orElseThrow(()->AirlineException.badRequest("CheckIn is still in progress...."));
    }

}