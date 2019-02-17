package za.co.weather.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import za.co.weather.bean.Data;
import za.co.weather.bean.WeatherData;
import za.co.weather.response.WeatherResponse;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplTest {

    @Autowired
    private WeatherServiceImpl weatherServiceImpl;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetWeatherDetails() {
        when(restTemplate.getForObject("", WeatherData.class)).thenReturn(buildWeatherData());
        ResponseEntity<WeatherResponse> weatherDetails = weatherServiceImpl.getWeatherDetails("");
        assertThat(weatherDetails).isNotNull();
    }

    private WeatherData buildWeatherData() {
        ArrayList<Data> dataArrayList = new ArrayList<>();
        dataArrayList.add(Data.builder().dt_txt(new Date()).build());
        WeatherData weatherData = WeatherData.builder().cnt(40).message(0.0036).cod("200").list(dataArrayList).build();
        return weatherData;
    }


}
