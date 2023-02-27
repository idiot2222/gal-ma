package me.bogeun.galma.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Pitcher extends Player {

    @Column(nullable = false)
    private int votes;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "pitcher")
    private final List<PitcherStat> pitcherStats = new ArrayList<>();

    public Pitcher(String name, Handedness handedness, int backNumber) {
        super(name, handedness, backNumber);
    }

    public void vote() {
        this.votes++;
    }

}
