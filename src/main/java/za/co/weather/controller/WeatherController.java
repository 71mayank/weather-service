package za.co.weather.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.weather.response.WeatherResponse;
import za.co.weather.service.WeatherServiceImpl;

import java.util.List;

@RestController
@RequestMapping("weather")
@Api(value = "Weather Service ", description = "Operations related to weather forecast")
public class WeatherController {

    @Autowired
    WeatherServiceImpl weatherServiceImpl;

    @ApiOperation(value = "View latest weather forecast", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved weather details"),
            @ApiResponse(code = 401, message = "You are not authorized to view the weather details"),
            @ApiResponse(code = 403, message = "Access to the weather forecast you were trying is forbidden"),
            @ApiResponse(code = 404, message = "The weather details you were trying to reach is not found")
    })
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getLatestRates(@ApiParam(value = "Default City", required = true)
                                                          @RequestParam(value = "city", defaultValue = "Altstadt") String city) {
        return weatherServiceImpl.getWeatherDetails(city);
    }

}
