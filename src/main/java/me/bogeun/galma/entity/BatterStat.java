package me.bogeun.galma.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Entity
public class BatterStat extends PlayerStat {

    @ManyToOne(fetch = FetchType.LAZY)
    private Batter batter;

    private int hit;

    private int hr;

    private int rbi;

    private int runs;

    private int sb;

    private int bb;

    private int k;

    @Column(nullable = false, length = 5)
    private String avg;

    @Column(nullable = false, length = 5)
    private String obp;

    @Column(nullable = false, length = 5)
    private String slg;

    @Override
    public String getFormatStat() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(batter.getName()).append(" ").append(getYear()).append("시즌]").append("<br/>");
        sb.append("안타/홈런/사사구 : ").append(hit).append(" / ").append(hr).append(" / ").append(bb).append("<br/>");
        sb.append("타/출/장 : ").append(avg).append(" / ").append(obp).append(" / ").append(slg).append("<br/>");
        sb.append("WAR : ").append(getWar());

        return sb.toString();
    }

    public BatterStat() {
        super();
    }

    public BatterStat(int year, String war, Batter batter, int hit, int hr, int rbi, int runs, int sb, int bb, int k, String avg, String obp, String slg) {
        super(year, war);
        this.batter = batter;
        this.hit = hit;
        this.hr = hr;
        this.rbi = rbi;
        this.runs = runs;
        this.sb = sb;
        this.bb = bb;
        this.k = k;
        this.avg = avg;
        this.obp = obp;
        this.slg = slg;
    }
}
