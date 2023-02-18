package me.bogeun.galma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    C("포수", 2), B1("1루수", 3), B2("2루수", 4),
    B3("3루수", 5), SS("유격수", 6), LF("좌익수", 7),
    CF("중견수", 8), RF("우익수", 9), DH("지명타자", 10);

    private final String korean;
    private final int positionNum;
}
