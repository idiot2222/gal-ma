package me.bogeun.galma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class PlayerStat {

    @Id
    @GeneratedValue
    private Long id;

    private int year;

    @Column(nullable = false, length = 6)
    private String war;

    public PlayerStat(int year, String war) {
        this.year = year;
        this.war = war;
    }

    public abstract String getFormatStat();
}
