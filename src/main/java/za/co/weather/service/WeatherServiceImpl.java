package za.co.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.constant.WeatherConstant;
import za.co.weather.response.WeatherResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${weather.service.url}")
    private String weatherServiceUrl;
    private final RestTemplate restTemplate;

    public WeatherServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.setConnectTimeout(WeatherConstant.HTTP_REQUEST_CONNECTION_TIME_OUT).setConnectTimeout(
                WeatherConstant.HTTP_REQUEST_READ_TIME_OUT).build();
    }

    @Override
    public ResponseEntity<WeatherResponse> getWeatherDetails(String city) {
        WeatherResponse weatherResponse = new WeatherResponse();
        try {
            WeatherData weatherData = restTemplate.getForObject(weatherServiceUrl, WeatherData.class);
            String startDate = WeatherConstant.DATE_FORMATTER.format(weatherData.getList().stream().map(listItem -> listItem.getDt_txt()).collect(Collectors.toList()).get(0));
            logger.info("Considering startDate {} ", startDate);

            Collection<Data> weatherForDay = new ArrayList<>();
            Collection<Data> weatherForNight = new ArrayList<>();

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
            logger.info("Day Weather Collected {}", weatherForDay.size());
            logger.info("Night Weather Collected {}", weatherForNight.size());

            List<Double> dataForDayTempAverage = weatherForDay.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
            OptionalDouble averageDay = calculateAverageInDouble(dataForDayTempAverage);
            if (averageDay.isPresent()){
                float averageCelsiusFor3Days = (float) averageDay.getAsDouble() - WeatherConstant.FTEMP;
                weatherResponse.setAverageTempratureDayTime(Math.round(averageCelsiusFor3Days) + WeatherConstant.TEMP_DEGREE_CELCIUS);
                logger.info("Celsius: averageCelsiusFor3Days {} ", Math.round(averageCelsiusFor3Days));
            }

            List<Double> dataForNightTempAverage = weatherForNight.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
            OptionalDouble averageNight = calculateAverageInDouble(dataForNightTempAverage);

            if(averageNight.isPresent()) {
                float averageCelsiusFor3Nights = (float) averageNight.getAsDouble() - WeatherConstant.FTEMP;
                logger.info("Celsius: averageCelsiusFor3Nights {}  ", Math.round(averageCelsiusFor3Nights));
                weatherResponse.setAverageTempratureNightTime(Math.round(averageCelsiusFor3Nights) + WeatherConstant.TEMP_DEGREE_CELCIUS);
            }

            Collection<Data> preasureBetweenDateTime = getWeatherBetweenDateTime(
                    weatherData.getList(),
                    WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DEFAULT_START_TIME),
                    addDays(WeatherConstant.DATE_TIME_FORMATTER.parse(startDate + WeatherConstant.DEFAULT_END_TIME), WeatherConstant.DAYS));

            List<Double> collectedPressure = preasureBetweenDateTime.stream().map(pressureData -> pressureData.getMain().getPressure()).collect(Collectors.toList());
            OptionalDouble averageForPressure = calculateAverageInDouble(collectedPressure);
            if(averageForPressure.isPresent()) {
                double presureAverage = Math.round(averageForPressure.getAsDouble());
                logger.info("Pascal: presureAverage {}  ", Math.round(presureAverage));
                weatherResponse.setAveragePressure(presureAverage + WeatherConstant.PRESSURE_PASCAL);

            }
            weatherResponse.setResponseMessage(weatherData.getCity().getName() + " weather forecast for next " + WeatherConstant.DAYS + " Days ");
            return new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        } catch (Exception e) {
            weatherResponse.setResponseMessage("Weather forecast failed due to Parsing " + e.getMessage());
            weatherResponse = null;
            return new ResponseEntity<>(weatherResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
