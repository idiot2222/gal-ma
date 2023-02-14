package me.bogeun.galma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpposingTeam {
    DOO("두산", "두산 베어스"), SSG("SSG", "SSG 랜더스"), KT("KT", "KT 위즈"),
    LG("LG", "LG 트윈스"), SAM("삼성", "삼성 라이온즈"), KIA("KIA", "KIA 타이거즈"),
    NC("NC", "NC 다이노스"), HH("한화", "한화 이글스"), KW("키움", "키움 히어로즈");

    private final String shortName;
    private final String fullName;

}
