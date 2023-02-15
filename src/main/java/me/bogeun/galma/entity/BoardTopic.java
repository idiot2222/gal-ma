package me.bogeun.galma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum BoardTopic {
    BASEBALL("야구"), INFO("정보"), HUMOR("유머"), HORROR("공포"), MATCH("경기");

    final String korean;

    public static BoardTopic toEnumType(String boardTopic) {
        return valueOf(boardTopic.toUpperCase(Locale.ROOT));
    }

}
