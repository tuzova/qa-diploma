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

class PaymentTest {

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
    void shouldClearData() {
        TestSQL.dropTables();
    }

    @Test
    @DisplayName("ID 01 | Valid | Card APPROVED")
    void shouldSuccessPayApprovedCard() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getApprovedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageApprovedPay(); // получить сообщение об успешной операции
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        var testStatus = TestSQL.getStatus(); // проверить: статус APPROVED
        assertEquals("APPROVED", testStatus);
    }

    @Test
    @DisplayName("ID 02-C | Valid | Card DECLINED")
    void shouldDeniedPayDeclinedCardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 01
    }

    @Test
    @DisplayName("ID 02-M | Valid | Card DECLINED")
    void shouldDeniedPayDeclinedCardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об отклонении операции
        // FAILED получено сообщение об успешной операции -> Issue 02
    }

    @Test
    @DisplayName("ID 03-C | Valid | Card 16 num")
    void shouldSuccessPayRandomCardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 03
    }

    @Test
    @DisplayName("ID 03-M | Valid | Card 16 num")
    void shouldSuccessPayRandomCardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено два сообщения одновременно - об отклонении и об успешной операции -> Issue 04
    }

    @Test
    @DisplayName("ID 04-C | Valid | Card 13 num")
    void shouldSuccessPayRandom13CardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 05
    }

    @Test
    @DisplayName("ID 04-M | Valid | Card 13 num")
    void shouldSuccessPayRandom13CardTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено сообщение о неверном формате -> Issue 06
    }

    @Test
    @DisplayName("ID 05 | Valid | Card 19 num")
    void shouldSuccessPayRandom19CardTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom19NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // PASSED but FAILED -> поле ограничено 16 символами -> Issue 07
    }

    @Test
    @DisplayName("ID 06-C | Valid | Future Year Not Over")
    void shouldSuccessPayFutureYearNotOverTestCount() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getFutureYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 08
    }

    @Test
    @DisplayName("ID 06-M | Valid | Future Year Not Over")
    void shouldSuccessPayFutureYearNotOverTestMessage() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getFutureYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено два сообщения одновременно - об отклонении и об успешной операции -> Issue 15
    }

    @Test
    @DisplayName("ID 07 | Invalid | Empty Card Number")
    void shouldDeniedPayEmptyCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 08 | Invalid | Empty Month")
    void shouldDeniedPayEmptyMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 09 | Invalid | Empty Year")
    void shouldDeniedPayEmptyYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 10 | Invalid | Empty CardOwner")
    void shouldDeniedPayEmptyCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 11 | Invalid | Empty CVC")
    void shouldDeniedPayEmptyCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getEmptyData()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> Issue 09
    }

    @Test
    @DisplayName("ID 12 | Invalid | Latin CardNumber")
    void shouldDeniedPayLatinCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 13 | Invalid | Latin Month")
    void shouldDeniedPayLatinMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 14 | Invalid | Latin Year")
    void shouldDeniedPayLatinYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 15 | Invalid | Latin CVC")
    void shouldDeniedPayLatinCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfLatinCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 16 | Invalid | Cyrillic CardNumber")
    void shouldDeniedPayCyrillicCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 17 | Invalid | Cyrillic Month")
    void shouldDeniedPayCyrillicMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 18 | Invalid | Cyrillic Year")
    void shouldDeniedPayCyrillicYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 19 | Invalid | Cyrillic CardOwner")
    void shouldDeniedPayCyrillicCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 10
    }

    @Test
    @DisplayName("ID 20 | Invalid | Cyrillic CVC")
    void shouldDeniedPayCyrillicCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfCyrillicCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 21 | Invalid | Random Chars CardNumber")
    void shouldDeniedPayRandomCharsCardNumber() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 22 | Invalid | Random Chars Month")
    void shouldDeniedPayRandomCharsMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 23 | Invalid | Random Chars Year")
    void shouldDeniedPayRandomCharsYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 24 | Invalid | Random Chars CardOwner")
    void shouldDeniedPayRandomCharsCardOwner() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 11
    }

    @Test
    @DisplayName("ID 25 | Invalid | Random Chars CVC")
    void shouldDeniedPayRandomCharsCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfRandomCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        payPage.messageWrongDataCardOwnerEmpty(); // нет сообщения о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 26 | Invalid | One Num Month")
    void shouldDeniedPayOneNumMonth() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getOneNumber(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 27 | Invalid | Month 00")
    void shouldDeniedPay00Month() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth00(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 12
    }

    @Test
    @DisplayName("ID 28 | Invalid | Month 13")
    void shouldDeniedPay13Month() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth13(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth13(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 29 | Invalid | Year Over")
    void shouldDeniedPayYearOver() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getYearOver(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongOverYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 30 | Invalid | Last Year")
    void shouldDeniedPayLastYear() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getLastYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongLastYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 31 | Invalid | CardOwner Over")
    void shouldDeniedPayCardOwnerOver() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getCardOwnerOver(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 32 | Invalid | One Num CVC")
    void shouldDeniedPayOneNumCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getOneNumber()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 33 | Invalid | Two Num CVC")
    void shouldDeniedPayTwoNumCvc() {

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getTwoNumber()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }
}