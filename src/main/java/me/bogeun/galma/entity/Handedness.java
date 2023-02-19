package me.bogeun.galma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Handedness {
    RPRB("R", "R"), RPLB("R", "L"), RPSB("R", "S"),
    LPRB("L", "R"), LPLB("L", "L"), LPSB("L", "S");

    private final String pitchingHand;
    private final String battingHand;
}
