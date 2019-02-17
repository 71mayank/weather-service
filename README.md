PREREQUISITE TO RUN THE weather-service

    1. Git version control software should be installed in the System
    2. Maven should be installed and setup correctly in environment variable.

STEPS TO RUN ON LOCAL

    1. Checkout weather service in your system using git clone https://github.com/71mayank/weather-service.git
    2. Navigate to weather-service folder run spring boot application using maven command mvn spring-boot:run
    3. Browse url http://localhost:1010/swagger-ui.html
    4. Hit end point /weather/data to view weather forecast

REQUIREMENT Develop REST API

    1. The API should expose a “/data” endpoint.
    2. The “/data” endpoint must include a CITY parameter containing the city’s name as the
       input for the correct response.
    3. The API with “/data” endpoint should retrieve the averages temperature (in Celsius) of the next 3 days from today’s date for Day time (06:00 – 18:00) and Night time (18:00 – 06:00)
    4. The API with “/data” endpoint should retrieve the average of pressure for the next 3 days from today’s date.


EXTERNAL REFEREBCE

    1. Given a dummy external API that gives weather metrics forecast data 5 day / 3 hour.

SOLUTION CONSIDERATIONS

    1. Parse weather metrics forecast data 5 day / 3 hour.
    2. Calculate day time average temperature by filtering each day metric between 06:00 - 18:00 for each day up to 3 days.
    3. Calculate day time average temperature by filtering each day metric between 18:00 - 06:00 for each day up to 3 days.
    4. Calculate average pressure by filtering each day metric between current date and current date+3 days.

EXAMPLE

     Start date 2017-02-16
     Day Start Time 06:00
     Day End Time 18:00
     Night Start Time 18:00
     Night End Time 06:00
    
     averageTempratureDayTime For Day 1 - Metric to be filtered between start date day start time and start date day end time 
        a. 2017-02-16 06:00:00 and 2017-02-16 18:00:00
     verageTempratureNightTime For Day 1 - Metric to be filtered between start date night start time and next day night end Time 
        b. 2017-02-16 18:00:00 and 2017-02-17 06:00:00
     averagePressure for Day 1 - Metric to filtered between start date and end date as (start date + 3 days)
        c. 2017-02-16 00:00:00 and 2017-02-19 00:00:00

CALCLUATION STEPS
    
     1. Collect metric for 3 days.
     2. Calculate averageTempratureDayTime
     3. Calculate averageTempratureNightTime
     4. Calculate averagePressure
     5. Wrap Response in entiry.
     6. Return ResponseEntiry.


OBSERVATIONS

    1. Using provided REST API https://openweathermap.org/forecast5 to get 5 day weather forecast
    2. API calls that are listed on this page are just samples and do not have any connection to the real API service!
    3. API call gives metric for every 3 hours
    4. API call gives metrics for 5 days.
    5. API call does not refresh data when changing city parameter.
    
NOTES

    1. Depending upon observations city parameter is not functional at the moment as external weather forecast API is a sample and not real.
    2. Calculations are done based on Altstadt city weather forecast time between 2017-02-16 12:00:00 and 2017-02-20 21:00:00
    

SAMPLE REQUEST AND RESPONSE

    1. Request    
        curl -X GET "http://localhost:1010/weather/data?city=Altstadt" -H "accept: application/json"
    
    2. Response body
        {
          "responseMessage": "Altstadt Weather Forecast retrieved successfully for next 3 Days ",
          "averageTempratureDayTime": "6'C",
          "averageTempratureNightTime": "0'C",
          "averagePressure": "967.0 Pascal"
        }
    
    3. Response headers    
         content-type: application/json;charset=UTF-8 
         date: Sun, 17 Feb 2019 09:13:33 GMT 
         transfer-encoding: chunked 
    
APPLICATION DEPENDENCIES

    1. JDK 8
    2. Spring Boot 2.0
    3. Spring Restful Service
    4. Spring fox Swagger 2.0
    5. Lombok
