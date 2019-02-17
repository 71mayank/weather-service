package za.co.weather.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.constant.WeatherConstant;

import java.text.ParseException;
import java.util.*;

@RunWith(SpringRunner.class)
public class WeatherServiceImplIntegrationTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public WeatherService weatherService() {
            return new WeatherServiceImpl(new RestTemplateBuilder());
        }
    }

    @Autowired
    WeatherServiceImpl weatherServiceImpl;

    @Test
    public void givenDays_whenAddDays_thenReturnNextDate() {
        Date date = new Date();
        Date newDate = weatherServiceImpl.addDays(date, 1);
        newDate.getTime();
        Assert.assertTrue(newDate.getTime() > date.getTime());
    }

    @Test
    public void givenList_whenCalculateAverageInDouble_thenReturnOptionalDouble() {
        List<Double> randomDoubleList = new ArrayList<>();
        randomDoubleList.add(266.6);
        randomDoubleList.add(256.7);
        randomDoubleList.add(296.3);
        randomDoubleList.add(268.6);
        Date date = new Date();
        OptionalDouble optionalDouble = weatherServiceImpl.calculateAverageInDouble(randomDoubleList);
        Assert.assertTrue(optionalDouble.isPresent());
        Assert.assertTrue(optionalDouble.getAsDouble() > 256.7);
    }

    @Test
    public void givenWeatherData_whenGetWeatherBetweenDateTime_thenReturnWeatherDetails() throws ParseException {
        String startDateTime = "2019-02-17 00:00:00";
        String endDateTime = "2020-02-18 00:00:00";
        WeatherData weatherData = new WeatherData();
        Data data = new Data();
        data.setDt_txt(new Date());
        List<Data> dataList = new ArrayList<>();
        dataList.add(data);
        weatherData.setList(dataList);
        Collection<Data> weatherBetweenDateTime = weatherServiceImpl.getWeatherBetweenDateTime(weatherData.getList(),
                WeatherConstant.DATE_TIME_FORMATTER.parse(startDateTime), WeatherConstant.DATE_TIME_FORMATTER.parse(endDateTime));
        Assert.assertTrue(!weatherBetweenDateTime.isEmpty());
    }

}
