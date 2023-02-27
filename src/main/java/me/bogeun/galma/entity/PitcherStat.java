package me.bogeun.galma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class PitcherStat extends PlayerStat {

    @ManyToOne(fetch = FetchType.LAZY)
    private Pitcher pitcher;

    private int w;

    private int l;

    private int h;

    private int sv;

    private int k;

    private int bb;

    @Column(nullable = false, length = 5)
    private String ip;

    @Column(nullable = false, length = 5)
    private String era;

    @Column(nullable = false, length = 4)
    private String whip;

    @Override
    public String getFormatStat() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(pitcher.getName()).append(" ").append(getYear()).append("시즌]").append("<br/>");
        sb.append("승/패/홀/세 : ").append(w).append("승 / ").append(l).append("패 / ").append(h).append("홀드 / ").append(sv).append("세이브").append("<br/>");
        sb.append("이닝/삼진/사사구 : ").append(ip).append(" / ").append(k).append(" / ").append(bb).append("<br/>");
        sb.append("ERA/WHIP : ").append(era).append(" / ").append(whip).append("<br/>");
        sb.append("WAR : ").append(getWar());

        return sb.toString();
    }

    public PitcherStat(int year, String war, Pitcher pitcher, int w, int l, int h, int sv, int k, int bb, String ip, String era, String whip) {
        super(year, war);
        this.pitcher = pitcher;
        this.w = w;
        this.l = l;
        this.h = h;
        this.sv = sv;
        this.k = k;
        this.bb = bb;
        this.ip = ip;
        this.era = era;
        this.whip = whip;
    }
}
