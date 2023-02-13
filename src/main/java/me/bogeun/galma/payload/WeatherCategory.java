package me.bogeun.galma.payload;

public enum WeatherCategory {

    // SKY = 1(맑음), 3(구름많음), 4(흐림)
    // PTY = 0(없음), 1(비), 2(비/눈), 3(눈), 4(소나기)
    TMP("기온"), WSD("풍속"), SKY("하늘"), POP("강수 확률"), PTY("강수 타입");

    final String korean;

    WeatherCategory(String korean) {
        this.korean = korean;
    }

}
