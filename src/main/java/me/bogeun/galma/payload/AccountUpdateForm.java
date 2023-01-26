package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountUpdateForm {

    private String nickname;

    @NotBlank
    @Length(min = 10, max = 30, message = "비밀번호는 10자 이상, 30자 이하입니다.")
    private String password;

    @Length(max = 100, message = "자기 소개는 100자 이하입니다.")
    private String description;

}
