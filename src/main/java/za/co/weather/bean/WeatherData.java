package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "cod",
        "message",
        "cnt",
        "list",
        "city"
})
public class WeatherData {
    Object cod;
    Object message;
    Object cnt;
    List<Data> list;
    City city;
}


