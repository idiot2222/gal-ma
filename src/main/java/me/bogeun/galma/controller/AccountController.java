package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.dto.SignUpDto;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.service.AccountService;
import me.bogeun.galma.utils.CurrentUser;
import me.bogeun.galma.validator.SignUpValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;

    private final SignUpValidator signUpValidator;


    @GetMapping("/login")
    public String getLogin() {
        return "account/login";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        model.addAttribute(new SignUpDto());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String postSignUp(@Valid @ModelAttribute SignUpDto signUpDto, Errors errors) {
        signUpValidator.validate(signUpDto, errors);
        if(errors.hasErrors()) {
            return "account/sign-up";
        }

        accountService.signUp(signUpDto);

        return "redirect:/login";
    }

    @GetMapping("/profile/{username}")
    public String getProfile(@PathVariable String username, Model model,
                             @CurrentUser Account currentAccount) {
        Account account = accountService.getAccountByUsername(username);

        model.addAttribute(account);
        model.addAttribute("isOwner", currentAccount.equals(account));

        return "account/profile";
    }

    @GetMapping("/test/1")
    public void test1() {
        throw new NoSuchElementException("no such");
    }

}
