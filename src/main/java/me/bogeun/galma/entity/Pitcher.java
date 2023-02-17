package me.bogeun.galma.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class Pitcher extends Player {

    @Column(nullable = false)
    private int votes;

    public Pitcher(Long id, String name, Handedness handedness, int backNumber, int votes) {
        super(id, name, handedness, backNumber);
        this.votes = votes;
    }

}
