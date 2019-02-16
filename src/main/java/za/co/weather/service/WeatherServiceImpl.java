package za.co.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.response.WeatherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private static final float FTEMP = 273.15F;
    private static final int DAYS = 3;
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static String dayStartTime = " 06:00:00";
    private static String dayEndTime = " 18:00:00";

    private static String nightStartTime = " 18:00:00";
    private static String nightEndTime = " 06:00:00";

    private static String pressureStartTime = " 01:00:00";
    private static String pressureEndTime = " 00:00:00";

    @Value("${weather.service.url}")
    private String weatherServiceUrl;
    private final RestTemplate restTemplate;

    public WeatherServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public WeatherResponse getWeatherDetails(String city) throws ParseException {
        WeatherResponse weatherExchangeRateResponse = new WeatherResponse();
        WeatherData weatherData = restTemplate.getForObject(weatherServiceUrl, WeatherData.class);

        Collection<Data> weatherForDay = new ArrayList<>();
        Collection<Data> weatherForNight = new ArrayList<>();

        String startDate  =DATE_FORMATTER.format(weatherData.getList().stream().map(listItem->listItem.getDt_txt()).collect(Collectors.toList()).get(0));
        logger.info("Found startDate {} ",startDate);

        for (int i = 0; i < DAYS; i++) {
            weatherForDay.addAll(getWeatherBetweenDateTime(
                    weatherData.getList(),
                    addDays(DATE_FORMATTER.parse(startDate + dayStartTime), i),
                    addDays(DATE_FORMATTER.parse(startDate + dayEndTime), i)));

            weatherForNight.addAll(getWeatherBetweenDateTime(
                    weatherData.getList(),
                    addDays(DATE_FORMATTER.parse(startDate + nightStartTime), i),
                    addDays(DATE_FORMATTER.parse(startDate + nightEndTime), i + 1)));

        }
        logger.info("Day Weather Ended " + weatherForDay.size());

        logger.info("Night Weather Ended " + weatherForNight.size());

        List<Object> dataForDayTempAverage = weatherForDay.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
        OptionalDouble averageDay = calculateAverageInDouble(dataForDayTempAverage);
        float averageCelsiusFor3Days = (float) averageDay.getAsDouble() - FTEMP;
        logger.info("Celsius: averageCelsiusFor3Days {} ", Math.round(averageCelsiusFor3Days));

        weatherExchangeRateResponse.setAverageTempratureDay(averageCelsiusFor3Days);

        List<Object> dataForNightTempAverage = weatherForNight.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
        OptionalDouble averageNight = calculateAverageInDouble(dataForNightTempAverage);
        float averageCelsiusFor3Nights = (float) averageNight.getAsDouble() - FTEMP;
        logger.info("Celsius: averageCelsiusFor3Nights {}  ", Math.round(averageCelsiusFor3Nights));

        weatherExchangeRateResponse.setAverageTempratureNight(averageCelsiusFor3Nights);

        Collection<Data> weatherBetweenDateTime = getWeatherBetweenDateTime(
                weatherData.getList(),
                DATE_FORMATTER.parse(startDate + pressureStartTime),
                addDays(DATE_FORMATTER.parse(startDate + pressureEndTime), 3));

        List<Object> collectedPressure = weatherBetweenDateTime.stream().map(pressureData -> pressureData.getMain().getPressure()).collect(Collectors.toList());
        OptionalDouble averageForPressure = calculateAverageInDouble(collectedPressure);

        weatherExchangeRateResponse.setAveragePressure((double) Math.round(averageForPressure.getAsDouble()));

        weatherExchangeRateResponse.setResponseMessage("Successfully calculated average ");

        return weatherExchangeRateResponse;
    }

    private static OptionalDouble calculateAverageInDouble(List<Object> averageData) {
        return averageData.stream()
                .mapToDouble(temprature -> (double) temprature)
                .average();
    }

    private static Collection<Data> getWeatherBetweenDateTime(Collection<Data> dataList, Date start, Date end) {
        return dataList.stream().filter(data -> data.getDt_txt().after(start) && data.getDt_txt().before(end)).collect(Collectors.toList());
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }


}
