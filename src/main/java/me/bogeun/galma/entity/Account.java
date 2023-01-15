package me.bogeun.galma.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, unique = true)
    private String username;

    @Column(length = 68)
    private String password;

    @Column(length = 50, unique = true)
    private String email;

    private LocalDateTime joinedAt;

    @Lob @Basic
    private String image;

    @Column(length = 100)
    private String description;


    @Builder
    public Account(String username, String password, String email, LocalDateTime joinedAt, String image, String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.joinedAt = joinedAt;
        this.image = image;
        this.description = description;
    }
}
