package me.bogeun.galma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Batter extends Player {

    @ElementCollection
    private List<Position> positions = new ArrayList<>();

    public Batter(String name, Handedness handedness, int backNumber, List<Position> positions) {
        super(name, handedness, backNumber);
        this.positions = positions;
    }

}
