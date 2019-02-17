package za.co.weather.controller;

import io.swagger.annotations.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.weather.response.WeatherResponse;
import za.co.weather.service.WeatherServiceImpl;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("weather")
@Api(value = "Weather Service ", description = "Operations related to weather forecast")
@Validated
public class WeatherController {

    @Autowired
    WeatherServiceImpl weatherServiceImpl;

    @ApiOperation(value = "View weather forecast", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved weather forecast"),
            @ApiResponse(code = 401, message = "You are not authorized to view the weather forecast"),
            @ApiResponse(code = 403, message = "Access to the weather forecast you were trying is forbidden"),
            @ApiResponse(code = 404, message = "The weather forecast you were trying to check is not found")
    })
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherForecast(@ApiParam(value = "Default City", required = true)
                                                          @RequestParam(value = "city")
                                                          @NonNull
                                                          @Size(min = 3, max = 50, message = "city length must be between 3 and 50") String city) {
        if (StringUtils.isEmpty(city)) {
            return new ResponseEntity<>(WeatherResponse.builder().responseMessage("City request parameter is missing").build(), HttpStatus.BAD_REQUEST);
        } else if (StringUtils.isNumeric(city)) {
            return new ResponseEntity<>(WeatherResponse.builder().responseMessage("City can not be a number").build(), HttpStatus.BAD_REQUEST);
        } else {
            return weatherServiceImpl.getWeatherDetails(city);
        }
    }

}
