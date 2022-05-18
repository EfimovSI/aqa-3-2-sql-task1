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

import java.util.ArrayList;
import java.util.Random;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    public static ArrayList<User> users = new ArrayList<>();

    @BeforeAll
    static void generateDb() {
        DbUtils.clearDb();
        for (int i = 0; i < 10; i++) {
            users.add(DbUtils.addNewUserToDb());
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
        User user = users.get(new Random().nextInt(users.size() - 1));
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
        User user = new User("", users.get(new Random().nextInt(users.size() - 1)).getLogin(), "");
        var loginPage = new LoginPage();
        loginPage.emptyPassword(user);
    }

    @Test
    void shouldNotLoginByInvalidPassword() {
        User user = new User("", users.get(new Random().nextInt(users.size() - 1)).getLogin(), "123");
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByUnexistingLoginWithExistingPassword() {
        User user = new User("", "login", users.get(new Random().nextInt(users.size() - 1)).getPassword());
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
    }

    @Test
    void shouldNotLoginByInvalidVerificationCode() {
        User user = users.get(new Random().nextInt(users.size() - 1));
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
    }

    @Test
    void shouldBlockByTripleInvalidLogin() {
        User user = users.get(new Random().nextInt(users.size() - 1));
        var loginPage = new LoginPage();
        loginPage.invalidLogin(user);
        loginPage.invalidLogin(user);
        loginPage.thirdInvalidLogin(user);
    }

    @Test
    void shouldBlockByTripleInvalidVerify() {
        User user = users.get(new Random().nextInt(users.size() - 1));
        var loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify();
        verificationPage.invalidVerify();
        verificationPage.thirdInvalidVerify();
    }
}
