package me.bogeun.galma.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(length = 20, unique = true)
    private String nickname;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }




    public boolean isChangeableNickname(int nicknameChangeDays) {
        LocalDateTime t = joinedAt.plusDays(nicknameChangeDays);

        return t.compareTo(LocalDateTime.now()) < 0;
    }

}
