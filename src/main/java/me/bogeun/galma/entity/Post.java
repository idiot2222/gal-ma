package me.bogeun.galma.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Size(max = 65535)
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardTopic boardTopic;

    @Column(nullable = false, updatable = false)
    private LocalDateTime wroteAt;

    private LocalDateTime modifiedAt;

    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    @OneToOne
    private Account writer;

    @Builder
    public Post(String title, String content, BoardTopic boardTopic, LocalDateTime wroteAt, LocalDateTime modifiedAt, Account writer) {
        this.title = title;
        this.content = content;
        this.boardTopic = boardTopic;
        this.wroteAt = wroteAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }
}
