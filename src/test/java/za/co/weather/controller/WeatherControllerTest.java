package za.co.weather.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import za.co.weather.response.WeatherResponse;
import za.co.weather.service.WeatherServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WeatherControllerTest {

    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    WeatherController weatherController;

    @MockBean
    WeatherServiceImpl weatherServiceImpl;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(this.weatherController).build();
    }

    @Test
    public void testGetWeatherDetailsOk() throws Exception {
        ResponseEntity<WeatherResponse> weatherDataResponseEntity = new ResponseEntity<>(new WeatherResponse(), HttpStatus.OK);
        when(weatherServiceImpl.getWeatherDetails(any(String.class))).thenReturn(weatherDataResponseEntity);
        mockMvc.perform(get("/weather/data").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetWeatherDetailsInternalServerError() throws Exception {
        ResponseEntity<WeatherResponse> weatherDataResponseEntity = new ResponseEntity<>(new WeatherResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        when(weatherServiceImpl.getWeatherDetails(any(String.class))).thenReturn(weatherDataResponseEntity);
        mockMvc.perform(get("/weather/data").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


}


