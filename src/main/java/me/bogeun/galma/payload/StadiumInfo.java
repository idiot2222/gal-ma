package me.bogeun.galma.payload;

import lombok.Getter;

@Getter
public enum StadiumInfo {
    SJ("사직", 98, 76),
    JS("잠실", 59,126),
    MH("문학", 55,124),
    DG("대구", 89,91),
    CW("창원", 91,76),
    DJ("대전", 67,101),
    GJ("광주", 59,75),
    GC("고척", 99,75),
    SW("수원", 60,121);

    private String name;
    private int coordinateX;
    private int coordinateY;

    StadiumInfo(String name, int coordinateX, int coordinateY) {
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }
}
