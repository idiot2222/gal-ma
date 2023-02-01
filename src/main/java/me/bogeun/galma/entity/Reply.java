package me.bogeun.galma.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Reply {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime wroteAt;

    private LocalDateTime modifiedAt;

    @JoinColumn(updatable = false)
    @OneToOne
    private Account account;

    @JoinColumn(updatable = false)
    @ManyToOne
    private Post post;


    @Builder
    public Reply(String content, LocalDateTime wroteAt, LocalDateTime modifiedAt, Account account, Post post) {
        this.content = content;
        this.wroteAt = wroteAt;
        this.modifiedAt = modifiedAt;
        this.account = account;
        this.post = post;
    }
}
