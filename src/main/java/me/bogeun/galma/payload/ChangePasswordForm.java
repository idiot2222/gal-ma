package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswordForm {

    @Length(min = 10, max = 30, message = "비밀번호는 10자 이상, 30자 이하입니다.")
    private String newPassword;

    private String newPasswordConfirm;

    private String passwordVerify;

}
