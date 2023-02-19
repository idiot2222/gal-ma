package me.bogeun.galma.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class BatterVote {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 2)
    private String keyword;

    @Column(nullable = false)
    private int voteCount;

    @ManyToOne
    private Batter batter;

    @Builder
    public BatterVote(String keyword, int voteCount, Batter batter) {
        this.keyword = keyword;
        this.voteCount = voteCount;
        this.batter = batter;
    }

    public void vote() {
        this.voteCount = this.voteCount + 1;
    }
}
