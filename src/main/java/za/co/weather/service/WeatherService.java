package za.co.weather.service;

import org.springframework.http.ResponseEntity;
import za.co.weather.response.WeatherResponse;

public interface WeatherService {
    ResponseEntity<WeatherResponse> getWeatherDetails(String city);
}
