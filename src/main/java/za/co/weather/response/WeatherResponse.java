package za.co.weather.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private String responseMessage;
    private String averageTempratureDayTime;
    private String averageTempratureNightTime;
    private String averagePressure;
}
