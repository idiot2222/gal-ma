package me.bogeun.galma.validator;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.dto.SignUpDto;
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
        return clazz.isAssignableFrom(SignUpDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) target;

        if(accountRepository.existsByUsername(signUpDto.getUsername())) {
            errors.rejectValue("username", "duplicated username.", "이미 사용중인 아이디입니다.");
        }

        if(accountRepository.existsByEmail(signUpDto.getEmail())) {
            errors.rejectValue("email", "duplicated email.", "이미 사용중인 이메일입니다.");
        }
    }
}
