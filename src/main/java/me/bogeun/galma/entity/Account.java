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

    @Column(updatable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime nicknameChangedAt;

    @Lob @Basic(fetch = FetchType.LAZY)
    private String image;

    @Column(length = 100)
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT", length=1)
    private boolean isPublicEmail;

    @Column(nullable = false, columnDefinition = "TINYINT", length=1)
    private boolean isPublicDescription;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @Builder
    public Account(String username, String nickname, String password, String email, LocalDateTime joinedAt, LocalDateTime nicknameChangedAt, String image, String description, boolean isPublicEmail, boolean isPublicDescription, UserRole userRole) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.joinedAt = joinedAt;
        this.nicknameChangedAt = nicknameChangedAt;
        this.image = image;
        this.description = description;
        this.isPublicEmail = isPublicEmail;
        this.isPublicDescription = isPublicDescription;
        this.userRole = userRole;
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
        LocalDateTime t = nicknameChangedAt.plusDays(nicknameChangeDays);

        return t.compareTo(LocalDateTime.now()) < 0;
    }

    public void changeImage(String image) {
        this.image = image;
    }
}
