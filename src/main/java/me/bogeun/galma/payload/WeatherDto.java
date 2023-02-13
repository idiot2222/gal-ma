package me.bogeun.galma.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@AllArgsConstructor
public class WeatherDto {

    private LocalDateTime dateTime;

    private final Map<WeatherCategory, String> dataMap = new HashMap<>();

    public void addData(WeatherCategory category, String value) {
        dataMap.put(category, value);
    }

}
