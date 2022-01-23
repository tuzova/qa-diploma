package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.sql.TestSQL;
import ru.netology.page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        open("http://localhost:8080");
    }

    @BeforeEach
    void shouldClearData() {
        TestSQL.dropTables();
    }

    // Позитивные сценарии тестирования

    @Test
    @DisplayName("ID 01")
    void shouldSuccessPayApprovedCard() { // Валидные данные, успешная операция

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
    @DisplayName("ID 02-testCount")
    void shouldDeniedPayDeclinedCardTestCount() { // Валидные данные, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> Issue 01
    }

    @Test
    @DisplayName("ID 02-message")
    void shouldDeniedPayDeclinedCardTestMessage() { // Валидные данные, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getDeclinedData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об отклонении операции
        // FAILED получено сообщение об успешной операции -> БАГ -> Issue 02
    }

    @Test
    @DisplayName("ID 03-testCount")
    void shouldSuccessPayRandomCardTestCount() { // В поле Номер карты ввести 16-тизначный номер, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> БАГ -> Issue 03
    }

    @Test
    @DisplayName("ID 03-message")
    void shouldSuccessPayRandomCardTestMessage() { // В поле Номер карты ввести 16-тизначный номер, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        var authInfo = DataHelper.getRandomData(); // обратиться к валидным данным
        payPage.payment(authInfo.getCardNumber(), authInfo.getMonth(), authInfo.getYear(), authInfo.getCardOwner(), authInfo.getCvc()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено два сообщения одновременно - об отклонении и об успешной операции -> БАГ -> Issue 04
    }

    @Test
    @DisplayName("ID 04-testCount")
    void shouldSuccessPayRandom13CardTestCount() { // В поле Номер карты ввести 13-тизначный номер, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> БАГ -> Issue 05
    }

    @Test
    @DisplayName("ID 04-message")
    void shouldSuccessPayRandom13CardTestMessage() { // В поле Номер карты ввести 13-тизначный номер, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom13NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        payPage.messageDeniedPay(); // получить сообщение об успешной операции
        // FAILED получено сообщение о неверном формате -> БАГ -> Issue 06
    }

    @Test
    @DisplayName("ID 05")
    void shouldSuccessPayRandom19CardTestCount() { // В поле Номер карты ввести 19-тизначный номер, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandom19NumberCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // PASSED but FAILED -> поле ограничено 16 символами -> БАГ -> Issue 07
    }

    @Test
    @DisplayName("ID 06")
    void shouldSuccessPayFutureYearNotOver() { // Номер года будущий, но не превышающий +5 лет, успешная операция

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getFutureYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные внесены в БД
        assertEquals(1, testCount);
        // FAILED данные не внесены в БД -> БАГ -> Issue 08
    }


    // Негативные сценарии тестирования

    @Test
    @DisplayName("ID 07")
    void shouldDeniedPayEmptyCardNumber() { // Незаполненное поле, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 08")
    void shouldDeniedPayEmptyMonth() { // Незаполненное поле, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 09")
    void shouldDeniedPayEmptyYear() { // Незаполненное поле, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 10")
    void shouldDeniedPayEmptyCardOwner() { // Незаполненное поле, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getEmptyData(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 11")
    void shouldDeniedPayEmptyCvc() { // Незаполненное поле, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getEmptyData()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 12")
    void shouldDeniedPayLatinCardNumber() { // Ввод латинских символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 13")
    void shouldDeniedPayLatinMonth() { // Ввод латинских символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 14")
    void shouldDeniedPayLatinYear() { // Ввод латинских символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfLatinCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 15")
    void shouldDeniedPayLatinCvc() { // Ввод латинских символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfLatinCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 16")
    void shouldDeniedPayCyrillicCardNumber() { // Ввод кириллических символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 17")
    void shouldDeniedPayCyrillicMonth() { // Ввод кириллических символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 18")
    void shouldDeniedPayCyrillicYear() { // Ввод кириллических символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 19")
    void shouldDeniedPayCyrillicCardOwner() { // Ввод кириллических символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfCyrillicCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 10
    }

    @Test
    @DisplayName("ID 20")
    void shouldDeniedPayCyrillicCvc() { // Ввод кириллических символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfCyrillicCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 21")
    void shouldDeniedPayRandomCharsCardNumber() { // Ввод произвольных символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardNumber(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 22")
    void shouldDeniedPayRandomCharsMonth() { // Ввод произвольных символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 23")
    void shouldDeniedPayRandomCharsYear() { // Ввод произвольных символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 24")
    void shouldDeniedPayRandomCharsCardOwner() { // Ввод произвольных символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getSetOfRandomCharacters(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 11
    }

    @Test
    @DisplayName("ID 25")
    void shouldDeniedPayRandomCharsCvc() { // Ввод произвольных символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getSetOfRandomCharacters()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
        // PASSED but FAILED получено два сообщения одновременно - в поле Владелец и поле CVC -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 26")
    void shouldDeniedPayOneNumMonth() { // Месяц ввести однозначный номер, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getOneNumber(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 27")
    void shouldDeniedPay00Month() { // Месяц ввести несуществующий номер, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth00(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 12
    }

    @Test
    @DisplayName("ID 28")
    void shouldDeniedPay13Month() { // Месяц ввести несуществующий номер, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.InValidData.getMonth13(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataMonth13(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 29")
    void shouldDeniedPayWrongYear() { // Номер года превышает +5 лет от текущего, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getYearOver(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongOverYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 30")
    void shouldDeniedPayLastYear() { // Номер года прошедший, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.InValidData.getLastYear(), DataHelper.ValidData.getCardOwner(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongLastYear(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 31")
    void shouldDeniedPayCardOwnerOver() { // Проверка поля Владелец на ограничение символов, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.InValidData.getCardOwnerOver(), DataHelper.ValidData.getCVC()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCardOwner(); // получить сообщение о неверном формате
        // FAILED нет ошибки формата -> БАГ -> Issue 09
    }

    @Test
    @DisplayName("ID 32")
    void shouldDeniedPayOneNumCvc() { // CVC ввести однозначный номер, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getOneNumber()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }

    @Test
    @DisplayName("ID 33")
    void shouldDeniedPayTwoNumCvc() { // CVC ввести двухзначный номер, операция отклонена

        var homePage = new HomePage(); // открыть homePage
        var payPage = homePage.buyButton(); // нажать на кнопку Купить -> переход на PayPage
        payPage.payment(DataHelper.ValidData.getRandomCard(), DataHelper.ValidData.getMonth(), DataHelper.ValidData.getYear(), DataHelper.ValidData.getCardOwner(), DataHelper.InValidData.getTwoNumber()); // вставить данные
        var testCount = TestSQL.getCountPayments(); // проверить: данные не внесены в БД
        assertEquals(0, testCount);
        payPage.messageWrongDataCvc(); // получить сообщение о неверном формате
    }
}