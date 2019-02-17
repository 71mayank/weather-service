package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class Weather {
    Object id;
    Object main;
    Object description;
    Object icon;
}
