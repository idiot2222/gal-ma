package me.bogeun.galma.utils;

import me.bogeun.galma.payload.StadiumInfo;
import me.bogeun.galma.payload.WeatherCategory;
import me.bogeun.galma.payload.WeatherDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WeatherDataSetting {

    @Value("${api.weather.key}")
    private String weatherKey;

    @Value("${api.weather.uri}")
    private String weatherUri;

    private final String baseTime = "0200";

    public List<WeatherDto> getWeather(StadiumInfo stadium) throws IOException, ParseException {
        URL url = getUrl(stadium);
        URLConnection urlConnection = url.openConnection();
        String json = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8)).readLine();

        return convertJsonToWeatherDto(json);
    }

    private List<WeatherDto> convertJsonToWeatherDto(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        return getWeatherDtoList(item);
    }

    /*
    * JSONArray 에서 필요한 아이템만 뽑아 리스트로 추출함
    * 필요 판단 기준은 WeatherCategory 에 존재 여부
    * */
    private List<WeatherDto> getWeatherDtoList(JSONArray item) {
        Map<LocalDateTime, WeatherDto> map = new HashMap<>();

        for (Object o : item) {
            JSONObject object = (JSONObject) o;
            String fcstDateTime = (String) object.get("fcstDate") + object.get("fcstTime");
            String category = (String) object.get("category");
            String value = (String) object.get("fcstValue");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDateTime dateTime = LocalDateTime.parse(fcstDateTime, dateTimeFormatter);
            WeatherCategory weatherCategory;

            try {
                weatherCategory = WeatherCategory.valueOf(category);
            } catch (IllegalArgumentException e) {
                continue;
            }

            if(!map.containsKey(dateTime)) {
                map.put(dateTime, new WeatherDto(dateTime));
            }

            WeatherDto weatherDto = map.get(dateTime);
            weatherDto.addData(weatherCategory, value);
        }

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private URL getUrl(StadiumInfo stadiumInfo) throws MalformedURLException {
        StringBuilder sb = new StringBuilder(weatherUri);
        LocalDateTime now = LocalDateTime.now();

        sb.append("?serviceKey=").append(weatherKey);
        sb.append("&pageNo=").append(1);
        sb.append("&numOfRows=").append(254); // 당일 0300 ~ 2300 시간대의 데이터 개수
        sb.append("&dataType=").append("JSON");
        sb.append("&base_date=").append(now.format(DateTimeFormatter.BASIC_ISO_DATE));
        sb.append("&base_time=").append(baseTime);
        sb.append("&nx=").append(stadiumInfo.getCoordinateX());
        sb.append("&ny=").append(stadiumInfo.getCoordinateY());

        return new URL(sb.toString());
    }


}
