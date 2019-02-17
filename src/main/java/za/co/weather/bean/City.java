package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    Object id;
    Object name;
    Coordinate coord;
    Object country;
}
