package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.User;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement loginSub = $("[data-test-id=login] .input__sub");
    private SelenideElement passwordSub = $("[data-test-id=password] .input__sub");

    public VerificationPage validLogin(User user) {
        loginField.val(user.getLogin());
        passwordField.val("qwerty123");
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(User user) {
        loginField.sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        loginField.val(user.getLogin());
        passwordField.val(user.getPassword());
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    public void emptyLogin(User user) {
        loginField.val(user.getLogin());
        passwordField.val(user.getPassword());
        loginButton.click();
        loginSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void emptyPassword(User user) {
        loginField.val(user.getLogin());
        passwordField.val(user.getPassword());
        loginButton.click();
        passwordSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void thirdInvalidLogin(User user) {
        loginField.sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        loginField.val(user.getLogin());
        passwordField.val(user.getPassword());
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Трижды неверно указан логин или пароль. Система заблокирована."));
    }
}
