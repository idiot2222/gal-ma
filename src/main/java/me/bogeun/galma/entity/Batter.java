package me.bogeun.galma.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Batter extends Player {

    @ElementCollection
    private List<Position> positions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "batter", orphanRemoval = true)
    private List<BatterStat> batterStats = new ArrayList<>();

    public Batter(String name, Handedness handedness, int backNumber, List<Position> positions) {
        super(name, handedness, backNumber);
        this.positions = positions;
    }

}
