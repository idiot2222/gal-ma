package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignUpForm {

    @NotBlank
    @Length(min = 5, max = 20, message = "아이디는 5자 이상, 20자 이하입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9-_]{5,20}$", message = "아이디에는 [알파벳 대소문자, 숫자, '-', '_']만 사용 가능합니다.")
    private String username;

    @NotBlank
    @Length(min = 10, max = 30, message = "비밀번호는 10자 이상, 30자 이하입니다.")
    private String password;

    @NotBlank
    @Length(min = 5, max = 50, message = "이메일은 5자 이상, 50자 이하입니다.")
    private String email;

}
