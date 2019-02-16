package za.co.weather.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    private String responseMessage;
    private Float averageTempratureDay;
    private Float averageTempratureNight;
    private Double averagePressure;
}
