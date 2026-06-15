package com.praveen.simulator.helper;

import com.praveen.simulator.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({AirlineException.class})
    public ResponseEntity<ApiResponse<Throwable>> handleException(AirlineException airlineException){
        return ApiResponse.error(airlineException.getMessage());
    }
}
