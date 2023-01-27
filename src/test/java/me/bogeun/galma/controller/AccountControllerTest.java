package me.bogeun.galma.controller;

import me.bogeun.galma.entity.Account;
import me.bogeun.galma.payload.SignUpForm;
import me.bogeun.galma.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    Account testAccount;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("nickname");
        signUpForm.setUsername("username");
        signUpForm.setPassword("password123");
        signUpForm.setEmail("user@email.com");

        testAccount = accountService.signUp(signUpForm);
    }


    @Test
    @DisplayName("GET 회원 가입 페이지")
    void getSignUp() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("POST 회원 가입 기능 - 성공")
    void postSignUpSuccess() throws Exception {
        mockMvc.perform(post("/sign-up").with(csrf())
                        .param("nickname", "test_nickname")
                        .param("username", "test_username")
                        .param("password", "test_password")
                        .param("email", "test@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    @DisplayName("POST 회원 가입 기능 - 실패")
    void postSignUpFail() throws Exception {
        mockMvc.perform(post("/sign-up").with(csrf())
                        .param("username", "**")
                        .param("password", "password")
                        .param("email", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @Test
    @DisplayName("GET 로그인 페이지")
    void getLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/login"));
    }

    @Test
    @DisplayName("POST 로그인 - 성공")
    void postLoginSuccess() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                        .param("username", "username")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("username"));
    }

    @Test
    @DisplayName("POST 로그인 - 실패")
    void postLoginFail() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                        .param("username", "wrong_username")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @Test
    @DisplayName("POST 로그아웃")
    void postLogout() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(unauthenticated());
    }
}