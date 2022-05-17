package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbUtils;
import ru.netology.data.User;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    @BeforeAll
    static void generateDb() {
        for (int i = 0; i < 10; i++) {
            DbUtils.addNewUserToDb();
        }
    }

    @BeforeEach
    void login() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void clear() {
        DbUtils.clearDb();
    }

    @Test
    void shouldLoginCorrectly() {
        User user = DbUtils.getRandomUserFromDb();
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        DashboardPage dashboardPage = verificationPage.validVerify(DbUtils.getVerificationCode());
        assertEquals("Личный кабинет", dashboardPage.getHeading());
    }

    @Test
    void shouldNotLoginWithEmptyFields() {
        User user = new User("","", "");
        var loginPage = new LoginPage();
        loginPage.emptyLogin(user);
    }

    @Test
    void shouldNotLoginByEmptyPassword() {
        User user = new User("",DbUtils.getRandomUserFromDb().getLogin(), "");
        var loginPage = new LoginPage();
        loginPage.emptyPassword(user);
    }

    @Test
    void shouldNotLoginByInvalidPassword() {
        User user = new User("",DbUtils.getRandomUserFromDb().getLogin(), "123");
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByUnexistingLoginWithExistingPassword() {
        User user = new User("", DataGenerator.generateUser().getLogin(), DbUtils.getRandomUserFromDb().getPassword());
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByInvalidVerificationCode() {
        User user = DbUtils.getRandomUserFromDb();
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
    }

    @Test
    void shouldBlockByTripleInvalidLogin() {
        User user = DbUtils.getRandomUserFromDb();
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
        loginPage.invalidLogin(user);
        loginPage.thirdInvalidLogin(user);
    }

    @Test
    void shouldBlockByTripleInvalidVerify() {
        User user = DbUtils.getRandomUserFromDb();
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
        verificationPage.invalidVerify();
        verificationPage.thirdInvalidVerify();
    }
}
