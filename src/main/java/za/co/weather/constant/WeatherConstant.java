package za.co.weather.constant;

import java.text.SimpleDateFormat;

public class WeatherConstant {

    public static final float FTEMP = 273.15F;
    public static final int DAYS = 3;
    public static final String TEMP_DEGREE_CELCIUS = "'C";
    public static final String PRESSURE_PASCAL = " Pascal";
    public static final int HTTP_REQUEST_CONNECTION_TIME_OUT = 15000 ;
    public static final int HTTP_REQUEST_READ_TIME_OUT = 15000 ;
    public static SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static String DAY_START_TIME = " 06:00:00";
    public static String DAY_END_TIME = " 18:00:00";

    public static String NIGHT_START_TIME = " 18:00:00";
    public static String NIGHT_END_TIME = " 06:00:00";

    public static String DEFAULT_START_TIME = " 00:00:00";
    public static String DEFAULT_END_TIME = " 00:00:00";
}
