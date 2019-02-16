package za.co.weather.bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MyWeatherMain {


    private static final float FTEMP = 273.15F;
    private static final int DAYS = 3;
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static String startDate = "2017-01-30";

    private static String dayStartTime = " 06:00:00";
    private static String dayEndTime = " 18:00:00";

    private static String nightStartTime = " 18:00:00";
    private static String nightEndTime = " 06:00:00";

    private static String pressureStartTime = " 01:00:00";
    private static String pressureEndTime = " 00:00:00";


    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "D:\\workspace\\Assignment\\currency-service\\src\\main\\java\\za\\co\\currency\\bean\\weather.json";
        WeatherData myWeather = mapper.readValue(new File(json), WeatherData.class);
        System.out.println(myWeather);

        Collection<Data> weatherForDay = new ArrayList<>();
        Collection<Data> weatherForNight = new ArrayList<>();

        for (int i = 0; i < DAYS; i++) {
            weatherForDay.addAll(getWeatherBetweenDateTime(
                    myWeather.getList(),
                    addDays(DATE_FORMATTER.parse(startDate + dayStartTime), i),
                    addDays(DATE_FORMATTER.parse(startDate + dayEndTime), i)));

            weatherForNight.addAll(getWeatherBetweenDateTime(
                    myWeather.getList(),
                    addDays(DATE_FORMATTER.parse(startDate + nightStartTime), i),
                    addDays(DATE_FORMATTER.parse(startDate + nightEndTime), i + 1)));

        }
        System.out.println("Day Weather Ended " + weatherForDay.size());
        System.out.println("Night Weather Ended " + weatherForNight.size());

        List<Object> dataForDayTempAverage = weatherForDay.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
        OptionalDouble averageDay = calculateAverageInDouble(dataForDayTempAverage);

        float averageCelsiusFor3Days = (float) averageDay.getAsDouble() - FTEMP;
        System.out.println("Celsius: " + Math.round(averageCelsiusFor3Days));

        List<Object> dataForNightTempAverage = weatherForNight.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
        OptionalDouble averageNight = calculateAverageInDouble(dataForNightTempAverage);

        float averageCelsiusFor3Nights = (float) averageNight.getAsDouble() - FTEMP;
        System.out.println("Celsius: " + Math.round(averageCelsiusFor3Nights));

        myWeather.getList().stream().map(listItem -> listItem.getMain()).collect(Collectors.toList());

        Collection<Data> weatherBetweenDateTime = getWeatherBetweenDateTime(
                myWeather.getList(),
                DATE_FORMATTER.parse(startDate + pressureStartTime),
                addDays(DATE_FORMATTER.parse(startDate + pressureEndTime), 3));

        List<Object> collectedPressure = weatherBetweenDateTime.stream().map(pressureData -> pressureData.getMain().getPressure()).collect(Collectors.toList());
        OptionalDouble averageForPressure = calculateAverageInDouble(collectedPressure);
        System.out.println(Math.round(averageForPressure.getAsDouble()));

       /* List<Object> dataForNightTempAverage = weatherForNight.stream().map(data -> data.getMain()).collect(Collectors.toList()).stream().map(main -> main.getTemp()).collect(Collectors.toList());
        dataForNightTempAverage.stream()
                .mapToDouble(temprature -> (double) temprature)
                .average()
                .ifPresent(System.out::println);*/



        /*for(int i=0;i<3;i++){
            Collection<Date> weatherForNight = getWeatherForDay(
                    dateList,
                    DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate + nightStartTime), i))),
                    DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate + nightEnd), i + 1))));
            System.out.println("weatherForNight  "+weatherForNight.size());
        }*/

        /*Collection<Date> day0 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(startDate+dayStartTime),
                DATE_FORMATTER.parse(startDate+dayEndTime));


        Collection<Date> day1 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+dayStartTime),1))),
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+dayEndTime),1))));

        Collection<Date> day2 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+dayStartTime),2))),
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+dayEndTime),2))));*/




        /*Collection<Date> night0 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(startDate+nightStartTime),
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+nightEnd),1))));


        Collection<Date> night1 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+nightStartTime),1))),
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+nightEnd),2))));

        Collection<Date> night2 = getWeatherForDay(
                dateList,
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+nightStartTime),2))),
                DATE_FORMATTER.parse(DATE_FORMATTER.DATE_FORMATTER(addDays(DATE_FORMATTER.parse(startDate+nightEnd),3))));*/

        System.out.println("Night Weather Ended");
    }

    private static OptionalDouble calculateAverageInDouble(List<Object> averageData) {
        return averageData.stream()
                .mapToDouble(temprature -> (double) temprature)
                .average();
    }

    private static Collection<Date> getWeatherForDay(Collection<Date> dateList, Date start, Date end) {
        Map<Object, Object> resultMap = new HashMap();

        dateList.stream()
                .filter(dates -> dates.after(start) && dates.before(end))
                .collect(Collectors.toList())
                .forEach(System.out::println);


        return dateList.stream()
                .filter(dates -> dates.after(start) && dates.before(end))
                .collect(Collectors.toList());
    }

    private static Collection<Date> getWeatherForDay3(Collection<Date> dateList, Date startDateTime, Date endDateTime) {
        Map<Object, Object> resultMap = new HashMap();

        dateList.stream()
                .filter(currentDateTime -> currentDateTime.compareTo(startDateTime) <= 0 && currentDateTime.compareTo(endDateTime) >= 0)
                .collect(Collectors.toList())
                .forEach(System.out::println);


        return dateList.stream()
                .filter(currentDateTime -> currentDateTime.compareTo(startDateTime) <= 0 && currentDateTime.compareTo(endDateTime) >= 0)
                .collect(Collectors.toList());
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





