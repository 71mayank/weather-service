package za.co.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.weather.bean.City;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.constant.WeatherConstant;
import za.co.weather.response.WeatherResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${weather.service.url}")
    private String weatherServiceUrl;
    private final RestTemplate restTemplate;

    public WeatherServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public WeatherResponse getWeatherDetails(String city) {
        WeatherResponse weatherExchangeRateResponse = new WeatherResponse();
        try {
            WeatherData weatherData = restTemplate.getForObject(weatherServiceUrl, WeatherData.class);
            Collection<Data> weatherForDay = new ArrayList<>();
            Collection<Data> weatherForNight = new ArrayList<>();
            String startDate = WeatherConstant.DATE_FORMATTER.format(weatherData.getList().stream().map(listItem -> listItem.getDt_txt()).collect(Collectors.toList()).get(0));
            logger.info("Found startDate {} ", startDate);

            for (int i = 0; i < WeatherConstant.DAYS; i++) {
                weatherForDay.addAll(getWeatherBetweenDateTime(
                        weatherData.getList(),
                        addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DAY_START_TIME), i),
                        addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DAY_END_TIME), i)));

                weatherForNight.addAll(getWeatherBetweenDateTime(
                        weatherData.getList(),
                        addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.NIGHT_START_TIME), i),
                        addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.NIGHT_END_TIME), i + 1)));

            }
            logger.info("Day Weather Ended " + weatherForDay.size());

            logger.info("Night Weather Ended " + weatherForNight.size());

            List<Double> dataForDayTempAverage = weatherForDay.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
            OptionalDouble averageDay = calculateAverageInDouble(dataForDayTempAverage);
            float averageCelsiusFor3Days = (float) averageDay.getAsDouble() - WeatherConstant.FTEMP;
            logger.info("Celsius: averageCelsiusFor3Days {} ", Math.round(averageCelsiusFor3Days));

            weatherExchangeRateResponse.setAverageTempratureDayTime(Math.round(averageCelsiusFor3Days) + WeatherConstant.TEMP_DEGREE_CELCIUS);

            List<Double> dataForNightTempAverage = weatherForNight.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
            OptionalDouble averageNight = calculateAverageInDouble(dataForNightTempAverage);
            float averageCelsiusFor3Nights = (float) averageNight.getAsDouble() - WeatherConstant.FTEMP;
            logger.info("Celsius: averageCelsiusFor3Nights {}  ", Math.round(averageCelsiusFor3Nights));

            weatherExchangeRateResponse.setAverageTempratureNightTime(Math.round(averageCelsiusFor3Nights) + WeatherConstant.TEMP_DEGREE_CELCIUS);

            Collection<Data> preasureBetweenDateTime = getWeatherBetweenDateTime(
                    weatherData.getList(),
                    WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DEFAULT_START_TIME),
                    addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DEFAULT_END_TIME), WeatherConstant.DAYS));

            List<Double> collectedPressure = preasureBetweenDateTime.stream().map(pressureData -> pressureData.getMain().getPressure()).collect(Collectors.toList());
            OptionalDouble averageForPressure = calculateAverageInDouble(collectedPressure);

            weatherExchangeRateResponse.setAveragePressure((double) Math.round(averageForPressure.getAsDouble()) + WeatherConstant.PRESSURE_PASCAL);

            weatherExchangeRateResponse.setResponseMessage( weatherData.getCity().getName()+ " Weather Forecast retrieved successfully for next "+WeatherConstant.DAYS+" Days ");

            return weatherExchangeRateResponse;

        } catch (ParseException pe) {
            weatherExchangeRateResponse.setResponseMessage("Weather forecast failed due to Parsing " + pe.getMessage());
        }
        return weatherExchangeRateResponse;
    }

    public OptionalDouble calculateAverageInDouble(List<Double> averageData) {
        return averageData.stream()
                .mapToDouble(temprature -> (double) temprature)
                .average();
    }

    public Collection<Data> getWeatherBetweenDateTime(Collection<Data> dataList, Date start, Date end) {
        return dataList.stream().filter(data -> data.getDt_txt().after(start) && data.getDt_txt().before(end)).collect(Collectors.toList());
    }

    public Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }


}
