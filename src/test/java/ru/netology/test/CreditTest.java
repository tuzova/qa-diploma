package ru.netology.test;

import ru.netology.data.DataHelper;
import ru.netology.sql.TestSQL;
import ru.netology.page.HomePage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.*;
import io.qameta.allure.selenide.AllureSelenide;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        open("http://localhost:8080");
    }

    @BeforeEach
    void shouldClearDataBefore() {
        TestSQL.dropTables();
    }

    @Test
    @DisplayName("ID 01C | Valid | Card APPROVED")
    void shouldSuccessPayApprovedCard() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getApprovedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageApprovedPay(); // получить сообщение об успешной операции
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        var testStatus = TestSQL.getStatusCredit(); // проверить: статус APPROVED
        assertEquals("APPROVED", testStatus);
    }

    @Test
    @DisplayName("ID 02C-C | Valid | Card DECLINED")
    void shouldDeniedPayDeclinedCardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 01
    }

    @Test
    @DisplayName("ID 02C-M | Valid | Card DECLINED")
    void shouldDeniedPayDeclinedCardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об отклонении операции
        // FAILED получено сообщение об успешной операции -> Issue 02
    }

    @Test
    @DisplayName("ID 03C-C | Valid | Card 16 num")
    void shouldSuccessPayRandomCardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 03
    }

    @Test
    @DisplayName("ID 03C-M | Valid | Card 16 num")
    void shouldSuccessPayRandomCardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено два сообщения одновременно - об отклонении и об успешной операции -> Issue 04
    }

    @Test
    @DisplayName("ID 04C-C | Valid | Card 13 num")
    void shouldSuccessPayRandom13CardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 05
    }

    @Test
    @DisplayName("ID 04C-M | Valid | Card 13 num")
    void shouldSuccessPayRandom13CardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено сообщение о неверном формате -> Issue 06
    }

    @Test
    @DisplayName("ID 05C | Valid | Card 19 num")
    void shouldSuccessPayRandom19CardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom19NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // PASSED but FAILED -> поле ограничено 16 символами -> Issue 07
    }

    @Test
    @DisplayName("ID 06C-C | Valid | Future Year Not Over")
    void shouldSuccessPayFutureYearNotOverTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getFutureYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 08
    }

    @Test
    @DisplayName("ID 06C-M | Valid | Future Year Not Over")
    void shouldSuccessPayFutureYearNotOverTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getFutureYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено два сообщения одновременно - об отклонении и об успешной операции -> Issue !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Test
    @DisplayName("ID 07C | Invalid | Empty Card Number")
    void shouldDeniedPayEmptyCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 08C | Invalid | Empty Month")
    void shouldDeniedPayEmptyMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 09C | Invalid | Empty Year")
    void shouldDeniedPayEmptyYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 10C | Invalid | Empty CardOwner")
    void shouldDeniedPayEmptyCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 11C | Invalid | Empty CVC")
    void shouldDeniedPayEmptyCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getEmptyData()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> Issue 09
    }

    @Test
    @DisplayName("ID 12C | Invalid | Latin CardNumber")
    void shouldDeniedPayLatinCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 13C | Invalid | Latin Month")
    void shouldDeniedPayLatinMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 14C | Invalid | Latin Year")
    void shouldDeniedPayLatinYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 15C | Invalid | Latin CVC")
    void shouldDeniedPayLatinCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfLatinCharacters()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 16C | Invalid | Cyrillic CardNumber")
    void shouldDeniedPayCyrillicCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 17C | Invalid | Cyrillic Month")
    void shouldDeniedPayCyrillicMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 18C | Invalid | Cyrillic Year")
    void shouldDeniedPayCyrillicYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 19C | Invalid | Cyrillic CardOwner")
    void shouldDeniedPayCyrillicCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 10
    }

    @Test
    @DisplayName("ID 20C | Invalid | Cyrillic CVC")
    void shouldDeniedPayCyrillicCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfCyrillicCharacters()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 21C | Invalid | Random Chars CardNumber")
    void shouldDeniedPayRandomCharsCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 22C | Invalid | Random Chars Month")
    void shouldDeniedPayRandomCharsMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 23C | Invalid | Random Chars Year")
    void shouldDeniedPayRandomCharsYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 24C | Invalid | Random Chars CardOwner")
    void shouldDeniedPayRandomCharsCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 11
    }

    @Test
    @DisplayName("ID 25C | Invalid | Random Chars CVC")
    void shouldDeniedPayRandomCharsCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfRandomCharacters()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 26C | Invalid | One Num Month")
    void shouldDeniedPayOneNumMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getOneNumber(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 27C | Invalid | Month 00")
    void shouldDeniedPay00Month() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth00(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 12
    }

    @Test
    @DisplayName("ID 28C | Invalid | Month 13")
    void shouldDeniedPay13Month() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth13(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth13(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 29C | Invalid | Year Over")
    void shouldDeniedPayYearOver() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getYearOver(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongOverYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 30C | Invalid | Last Year")
    void shouldDeniedPayLastYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getLastYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongLastYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 31C | Invalid | CardOwner Over")
    void shouldDeniedPayCardOwnerOver() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getCardOwnerOver(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 32C | Invalid | One Num CVC")
    void shouldDeniedPayOneNumCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getOneNumber()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 33C | Invalid | Two Num CVC")
    void shouldDeniedPayTwoNumCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyCreditButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getTwoNumber()); // вставить данные
        var testCount = TestSQL.getCountCredits(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }
}