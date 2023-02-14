package me.bogeun.galma.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bogeun.galma.payload.Stadium;
import me.bogeun.galma.payload.WeatherCategory;
import me.bogeun.galma.payload.WeatherDto;
import net.spy.memcached.MemcachedClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherDataSetting {

    private final MemcachedClient memcachedClient;

    @Value("${api.weather.key}")
    private String weatherKey;

    @Value("${api.weather.uri}")
    private String weatherUri;

    private final String BASE_TIME = "0200";

    private final String WEATHER_CACHE_KEY = "weather";

    @Value("${cache.weather.expiration}")
    private int weatherExp;


    public List<WeatherDto> getWeather(Stadium stadium) {
        String json = (String) memcachedClient.get(WEATHER_CACHE_KEY);

        if(json == null) {
            json = getDataFromExternalAPI(stadium);
        }

        return convertJsonToWeatherDto(json);
    }

    private String getDataFromExternalAPI(Stadium stadium) {
        String json;

        try {
            URL url = getUrl(stadium);
            URLConnection urlConnection = url.openConnection();

            log.info("외부 API 요청 시도 : {}", url);

            json = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8)).readLine();
        } catch (Exception e) {
            throw new IllegalArgumentException("external api error.");
        }

        memcachedClient.set(WEATHER_CACHE_KEY, weatherExp, json);

        return json;
    }

    private List<WeatherDto> convertJsonToWeatherDto(String json) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(json);
        } catch (ParseException e) {
            throw new IllegalArgumentException("json parse error.");
        }

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

    private URL getUrl(Stadium stadium) throws MalformedURLException {
        StringBuilder sb = new StringBuilder(weatherUri);
        LocalDateTime now = LocalDateTime.now();

        sb.append("?serviceKey=").append(weatherKey);
        sb.append("&pageNo=").append(1);
        sb.append("&numOfRows=").append(290); // 당일 0300 ~ 2500 시간대의 데이터 개수
        sb.append("&dataType=").append("JSON");
        sb.append("&base_date=").append(now.format(DateTimeFormatter.BASIC_ISO_DATE));
        sb.append("&base_time=").append(BASE_TIME);
        sb.append("&nx=").append(stadium.getCoordinateX());
        sb.append("&ny=").append(stadium.getCoordinateY());

        return new URL(sb.toString());
    }


}
