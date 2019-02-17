package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
    Double temp;
    Object temp_min;
    Object temp_max;
    Double pressure;
    Object sea_level;
    Object grnd_level;
    Object humidity;
    Object temp_kf;
}
