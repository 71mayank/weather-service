package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class Coordinate {
    Object lat;
    Object lon;
}