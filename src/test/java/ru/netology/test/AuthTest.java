package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DbUtils;
import ru.netology.data.User;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    @BeforeEach
    void login() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginCorrectly() {
        User user = new User("vasya", "qwerty123");
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        DashboardPage dashboardPage = verificationPage.validVerify(DbUtils.getVerificationCode());
        assertEquals("Личный кабинет", dashboardPage.getHeading());
    }

    @Test
    void shouldNotLoginWithEmptyFields() {
        User user = new User("", "");
        var loginPage = new LoginPage();
        loginPage.emptyLogin(user);
    }

    @Test
    void shouldNotLoginByEmptyPassword() {
        User user = new User("vasya", "");
        var loginPage = new LoginPage();
        loginPage.emptyPassword(user);
    }

    @Test
    void shouldNotLoginByInvalidPassword() {
        User user = new User("vasya", "123");
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByUnexistingLoginWithExistingPassword() {
        User user = new User("kolya", "qwerty123");
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByInvalidVerificationCode() {
        User user = new User("vasya", "qwerty123");
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
    }

    @Test
    void shouldBlockByTripleInvalidLogin() {
        User user = new User("vasya", "123");
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
        loginPage.invalidLogin(user);
        loginPage.thirdInvalidLogin(user);
    }

    @Test
    void shouldBlockByTripleInvalidVerify() {
        User user = new User("petya", "123qwerty");
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
        verificationPage.invalidVerify();
        verificationPage.thirdInvalidVerify();
    }
}
