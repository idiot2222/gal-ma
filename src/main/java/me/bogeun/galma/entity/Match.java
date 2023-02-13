package me.bogeun.galma.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bogeun.galma.payload.Stadium;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "MATCHES")
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpposingTeam opposingTeam;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Stadium matchStadium;

    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @Builder
    public Match(OpposingTeam opposingTeam, Stadium matchStadium, LocalDateTime matchDateTime) {
        this.opposingTeam = opposingTeam;
        this.matchStadium = matchStadium;
        this.matchDateTime = matchDateTime;
    }

}
