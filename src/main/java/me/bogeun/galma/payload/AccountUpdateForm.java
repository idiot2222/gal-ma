package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AccountUpdateForm {

    private String nickname;

    private String password;

    @Length(max = 100, message = "자기 소개는 100자 이하입니다.")
    private String description;

}
