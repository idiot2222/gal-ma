package me.bogeun.galma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@MappedSuperclass
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Handedness handedness;

    @Column(nullable = false, unique = true)
    private int backNumber;

    public Player(String name, Handedness handedness, int backNumber) {
        this.name = name;
        this.handedness = handedness;
        this.backNumber = backNumber;
    }
}
