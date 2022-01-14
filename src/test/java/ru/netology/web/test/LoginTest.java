package ru.netology.web.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class LoginTest {

    @AfterAll
    static void shouldClearData() {
        DataHelper.dropTables();
    }

    @Test
    void shouldSuccessfulValidLoginValidCode() {

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getValidVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldUnsuccessfulValidLoginInvalidCode() {

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getInvalidVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        verificationPage.warningMessageInvalidCode();
    }

    @Test
    void shouldUnsuccessfulInvalidLogin() {

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getInvalidAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.warningMessageLogin();
    }

    @Test
    void shouldUnsuccessfulExceedingLimit() { // баг: не появляется warningMessageLoginExceedingLimit

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getInvalidAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.warningMessageLogin();
        LoginPage.clearLoginFields();
        loginPage.invalidLogin(authInfo);
        loginPage.warningMessageLogin();
        LoginPage.clearLoginFields();
        loginPage.invalidLogin(authInfo);
        loginPage.warningMessageLoginExceedingLimit();
    }
}


