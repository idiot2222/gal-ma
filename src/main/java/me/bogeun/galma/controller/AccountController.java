package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.UserRole;
import me.bogeun.galma.payload.AccountUpdateForm;
import me.bogeun.galma.payload.ChangePasswordForm;
import me.bogeun.galma.payload.SignUpForm;
import me.bogeun.galma.service.AccountService;
import me.bogeun.galma.utils.CurrentUser;
import me.bogeun.galma.validator.AccountUpdateValidator;
import me.bogeun.galma.validator.SignUpValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;

    private final SignUpValidator signUpValidator;
    private final AccountUpdateValidator accountUpdateValidator;

    private final PasswordEncoder passwordEncoder;

    @Value("${config.nickname-change-days}")
    private int nicknameChangeDays;


    @GetMapping("/login")
    public String getLogin() {
        return "account/login";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String postSignUp(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {
        signUpValidator.validate(signUpForm, errors);
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        accountService.signUp(signUpForm, UserRole.COMMON);

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

    @GetMapping("/profile/{username}/update")
    public String getProfileUpdate(@PathVariable String username, Model model,
                                   @CurrentUser Account currentAccount) {
        Account account = accountService.getAccountByUsername(username);
        boolean isOwner = currentAccount.equals(account);

        checkPrincipal(isOwner);

        model.addAttribute(account);
        model.addAttribute(new AccountUpdateForm());
        model.addAttribute(new ChangePasswordForm());
        model.addAttribute("changeableNickname", account.isChangeableNickname(nicknameChangeDays));
        model.addAttribute("nicknameChangeDays", nicknameChangeDays);

        return "account/update-profile";
    }

    @PostMapping("/profile/{username}/update")
    public String postProfileUpdate(@PathVariable String username, @CurrentUser Account currentAccount, Model model,
                                    @Valid @ModelAttribute AccountUpdateForm updateForm, Errors errors) {

        checkPrincipal(username.equals(currentAccount.getUsername()));

        Account account = accountService.getAccountByUsername(username);
        if (account.getNickname().equals(updateForm.getNickname())) {
            updateForm.setNickname("");
        }

        if (!passwordEncoder.matches(updateForm.getPassword(), account.getPassword())) {
            errors.rejectValue("password", "invalid password.", "비밀번호가 틀렸습니다.");
        }

        accountUpdateValidator.validate(updateForm, errors);
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return "account/update-profile";
        }

        accountService.updateUserInfo(account, updateForm);

        return "redirect:/profile/" + username;
    }

    @GetMapping("/profile/{username}/update/account")
    public String getProfileUpdateAccount(@PathVariable String username, Model model) {
        model.addAttribute(new ChangePasswordForm());

        return "account/update-account";
    }

    @PostMapping("/profile/{username}/update/account")
    public String postProfileUpdateAccount(@PathVariable String username, @CurrentUser Account currentAccount,
                                 @Valid @ModelAttribute ChangePasswordForm passwordForm, Errors errors) {

        Account account = accountService.getAccountByUsername(username);

        checkPrincipal(username.equals(currentAccount.getUsername()));

        if(!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPasswordConfirm", "invalid password confirmation.", "비밀번호가 일치하지 않습니다.");
        }
        if (!passwordEncoder.matches(passwordForm.getPasswordVerify(), account.getPassword())) {
            errors.rejectValue("passwordVerify", "invalid password.", "비밀번호가 틀렸습니다.");
        }
        if(errors.hasErrors()) {
            return "account/update-account";
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());

        return "redirect:/profile/" + username;
    }

    @PostMapping("/profile/{username}/config")
    public String saveConfig(@PathVariable String username, @CurrentUser Account currentAccount,
                             @RequestParam(required = false) boolean isPublicEmail,
                             @RequestParam(required = false) boolean isPublicDescription) {

        checkPrincipal(username.equals(currentAccount.getUsername()));

        accountService.saveConfig(username, isPublicEmail, isPublicDescription);

        return "redirect:/profile/" + username;
    }

    @ResponseBody
    @PostMapping("/profile/{username}/image")
    public String setProfileImage(@PathVariable String username, @CurrentUser Account currentAccount,
                                  @RequestBody String image) {
        Account account = accountService.getAccountByUsername(username);
        if (!account.equals(currentAccount)) {
            throw new BadCredentialsException("have no access.");
        }

        accountService.setProfileImage(account, image);

        return "success";
    }



    private void checkPrincipal(boolean username) {
        if (!username) {
            throw new BadCredentialsException("have no access.");
        }
    }

}
