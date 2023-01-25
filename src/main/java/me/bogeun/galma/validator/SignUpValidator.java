package me.bogeun.galma.validator;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.payload.SignUpForm;
import me.bogeun.galma.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignUpValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;

        if(accountRepository.existsByUsername(signUpForm.getUsername())) {
            errors.rejectValue("username", "duplicated username.", "이미 사용중인 아이디입니다.");
        }

        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "duplicated email.", "이미 사용중인 이메일입니다.");
        }
    }
}
