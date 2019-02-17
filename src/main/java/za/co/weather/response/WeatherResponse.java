package za.co.weather.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    private String responseMessage;
    private String averageTempratureDayTime;
    private String averageTempratureNightTime;
    private String averagePressure;
}
