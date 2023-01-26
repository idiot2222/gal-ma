package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.payload.AccountUpdateForm;
import me.bogeun.galma.payload.SignUpForm;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.UserAccount;
import me.bogeun.galma.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public Account signUp(SignUpForm signUpForm) {
        Account account = Account.builder()
                .username(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .email(signUpForm.getEmail())
                .joinedAt(LocalDateTime.now())
                .nicknameChangedAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("invalid username."));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username."));

        return new UserAccount(account);
    }

    public void updateUserInfo(Account account, AccountUpdateForm updateForm) {
        if(!updateForm.getNickname().isBlank()) {
            account.setNickname(updateForm.getNickname());
            account.setNicknameChangedAt(LocalDateTime.now());
        }

        account.setDescription(updateForm.getDescription());
    }
}
