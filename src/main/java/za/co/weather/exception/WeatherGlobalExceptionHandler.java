package za.co.weather.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class WeatherGlobalExceptionHandler {

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(WeatherNotFoundException ex, WebRequest request) {
        WeatherErrorDetails weatherErrorDetails = new WeatherErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(weatherErrorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        WeatherErrorDetails weatherErrorDetails = new WeatherErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(weatherErrorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

