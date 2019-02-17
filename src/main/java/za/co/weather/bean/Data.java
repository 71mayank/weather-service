package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    Date dt;
    Main main;
    Weather[] weather;
    Cloud clouds;
    Wind wind;
    Object snow;
    Sys sys;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dt_txt;
}
