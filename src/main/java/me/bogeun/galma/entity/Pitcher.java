package me.bogeun.galma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Pitcher extends Player {

    @Column(nullable = false)
    private int votes;

    public Pitcher(String name, Handedness handedness, int backNumber) {
        super(name, handedness, backNumber);
    }

    public void vote() {
        this.votes++;
    }
}
