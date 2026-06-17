package com.praveen.simulator.helper;

import com.praveen.simulator.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({AirlineException.class})
    public ResponseEntity<ApiResponse<Throwable>> handleException(AirlineException airlineException){
        log.error("Can not process further due to {}",airlineException.getMessage());
        return ApiResponse.error(airlineException.getMessage());
    }
}
