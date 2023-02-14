package me.bogeun.galma.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Stadium {
    SJ("사직", "부산 사직 야구장", 98, 76),
    JS("잠실", "서울 잠실 야구장", 59,126),
    MH("문학", "인천 랜더스 파크", 55,124),
    DG("대구", "대구 라이온즈 파크", 89,91),
    CW("창원", "창원 NC 파크", 91,76),
    DJ("대전", "한화생명 이글스 파크", 67,101),
    GJ("광주", "기아 챔피언스 필드", 59,75),
    GC("고척", "고척 스카이돔", 99,75),
    SW("수원", "수원 KT 위즈 파크", 60,121);

    private final String shortName;
    private final String fullName;
    private final int coordinateX;
    private final int coordinateY;

}
