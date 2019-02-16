package za.co.weather.service;

import za.co.weather.exception.WeatherNotFoundException;
import za.co.weather.response.WeatherResponse;

import java.text.ParseException;

public interface WeatherService {
    WeatherResponse getWeatherDetails(String city) throws WeatherNotFoundException, ParseException;
}
