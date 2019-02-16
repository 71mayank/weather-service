package za.co.weather.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import za.co.weather.controller.WeatherController;
import za.co.weather.response.WeatherResponse;
import za.co.weather.service.WeatherServiceImpl;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)

public class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherServiceImpl weatherServiceImpl;

    @Test
    public void givenCity_whenData_thenReturnWeatherForecast() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse();
        given(weatherServiceImpl.getWeatherDetails("BTC")).willReturn(weatherResponse);
        mvc.perform(get("https://samples.openweathermap.org/data/2.5/forecast?q=M%C3%BCnchen,DE&appid=b6907d289e10d714a6e88b30761fae22")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
