package za.co.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
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
