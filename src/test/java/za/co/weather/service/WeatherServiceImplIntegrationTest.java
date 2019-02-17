package za.co.weather.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.constant.WeatherConstant;
import za.co.weather.response.WeatherResponse;

import java.text.ParseException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplIntegrationTest {

    @Autowired
    WeatherServiceImpl weatherServiceImpl;

    @Test
    public void testAddDays() {
        Date date = new Date();
        Date newDate = weatherServiceImpl.addDays(date, 1);
        newDate.getTime();
        Assert.assertTrue(newDate.getTime() > date.getTime());
    }

    @Test
    public void testCalculateAverageInDouble() {
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
    public void testGetWeatherBetweenDateTime() throws ParseException {
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


    @Test
    public void testGetWeatherDetails() {
        ResponseEntity<WeatherResponse> moscow = weatherServiceImpl.getWeatherDetails("Moscow");
        assertThat(moscow).isNotNull();
    }

}
