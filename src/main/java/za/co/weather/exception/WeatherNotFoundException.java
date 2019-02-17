package za.co.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WeatherNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    public WeatherNotFoundException(String message){
        super(message);
    }
}