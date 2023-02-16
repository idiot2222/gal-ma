package me.bogeun.galma.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class Pitcher extends Player {

    public Pitcher(String name, Handedness handedness, int backNumber) {
        super(name, handedness, backNumber);
    }

}
