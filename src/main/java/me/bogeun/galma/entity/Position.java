package me.bogeun.galma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    C("포수"), B1("1루수"), B2("2루수"),
    B3("3루수"), SS("유격수"), LF("좌익수"),
    CF("중견수"), RF("우익수"), DH("지명타자");

    private final String korean;
}
