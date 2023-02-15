package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.UserRole;
import me.bogeun.galma.payload.SignUpForm;
import me.bogeun.galma.service.AccountService;
import me.bogeun.galma.validator.SignUpValidator;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AccountService accountService;

    private final SignUpValidator signUpValidator;

    @PostMapping("/sign-up")
    public String postSignUp(@RequestBody SignUpForm signUpForm, Errors errors) {
        signUpValidator.validate(signUpForm, errors);
        if (errors.hasErrors()) {
            return errors.getAllErrors().toString();
        }

        accountService.signUp(signUpForm, UserRole.ADMIN);

        return "success";
    }
}
