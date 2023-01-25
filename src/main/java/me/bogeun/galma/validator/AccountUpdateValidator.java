package me.bogeun.galma.validator;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.payload.AccountUpdateForm;
import me.bogeun.galma.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class AccountUpdateValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountUpdateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountUpdateForm updateForm = (AccountUpdateForm) target;
        String nickname = updateForm.getNickname();
        String password = updateForm.getPassword();


        if(nickname.length() != 0 && nickname.length() < 3 || nickname.length() > 20) {
            errors.rejectValue("nickname", "invalid nickname.", "닉네임은 3자이상, 20자 이하입니다.");
        }
        if (accountRepository.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "duplicated nickname.", "이미 사용중인 닉네임입니다.");
        }

        if(password.length() != 0 && password.length() < 10) {
            errors.rejectValue("password", "invalid password.", "비밀번호는 10자 이상입니다.");
        }
    }
}
