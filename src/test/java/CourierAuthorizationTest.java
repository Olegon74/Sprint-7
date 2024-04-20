import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CourierAuthorizationTest {

    private ScooterClient client;
    private String id;

    @Before
    public void setUp() {

        client = new ScooterClientImpl();
    }


    @Test
    @DisplayName("The courier can log in with all required fields")
    @Description("Курьер может авторизоваться, передав все обязательные поля, позитивный тест")
    public void theCourierCanLogInWithAllRequiredFields() {


        Courier courier = Courier.create("testfrghyui", "12345", "Oleg");
        client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("Request with an incorrect login and password")
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку, негативный тест")
    public void requestWithAnIncorrectLoginAndPassword() {

        Courier courier = Courier.create("tedsdsyui", "12345", "Oleg");
        client.createCourier(courier);

        Credentials wrongCredentials = Credentials.withWrongCredentials("vcvcggfgfaa", "89898783434");
        ValidatableResponse loginResponse2 = client.login(wrongCredentials);

        loginResponse2
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");



    }

    @Test
    @DisplayName("Request with an incorrect login")
    @Description("Cистема вернёт ошибку, если неправильно указать логин, негативный тест")
    public void requestWithAnIncorrectLogin() {

        Courier courier = Courier.create("eerghgfrgsss", "12345", "Oleg");
        client.createCourier(courier);

        Credentials wrongCredentials = Credentials.withWrongCredentials("dfdfdfdfd", "12345");
        ValidatableResponse loginResponse2 = client.login(wrongCredentials);

        loginResponse2
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("Request with an incorrect password")
    @Description("Cистема вернёт ошибку, если неправильно указать пароль, негативный тест")
    public void requestWithAnIncorrectPassword() {

        Courier courier = Courier.create("tfdfdsdf", "12345", "Oleg");
        client.createCourier(courier);

        Credentials wrongCredentials = Credentials.withWrongCredentials("teswdsdf", "54321");
        ValidatableResponse loginResponse2 = client.login(wrongCredentials);

        loginResponse2
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("Successful login returns an ID")
    @Description("При успешной авторизации возвращается ID курьера")
    public void successfulLoginReturnsId() {
        // Создаем нового курьера
        Courier courier = Courier.create("tejklfn", "testPassword", "Oleg");
        client.createCourier(courier);

        // Авторизуемся с созданным курьером
        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");

        int id = loginResponse.extract().jsonPath().getInt("id");

        // Проверяем, что ID возвращается и является положительным числом
        assertTrue("Courier ID should be a positive number", id > 0);




    }

    @Test
    @DisplayName("Request without login field returns an error")
    @Description("Тест проверяет, что при отсутствии поля login в запросе возвращается ошибка")
    public void requestWithoutLoginFieldReturnsError() {
        // Создаем курьера для проверки
        Courier courier = Courier.create("tebnbnnbLogin", "12345", "Oleg");
        client.createCourier(courier);

        // Отправляем запрос без поля login
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("password", "12345");
        requestBody.put("firstName", "Oleg");
        ValidatableResponse errorResponse = client.sendRequestWithoutLoginField(requestBody);

        // Проверяем, что возвращается ожидаемая ошибка
        errorResponse
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");

    }

    @Test

    @DisplayName("Request without password field returns an error")
    @Description("Тест проверяет, что при отсутствии поля password в запросе возвращается ошибка")
    @Ignore("504 ошибка от сервера, тест должен проходить")
    public void requestWithoutPasswordFieldReturnsError() {
        // Создаем курьера для проверки
        Courier courier = Courier.create("yhduLhgogin", "12343", "Oleg");
        client.createCourier(courier);

        // Отправляем запрос без поля password
        Map<String, String>requestBody = new HashMap<>();
        requestBody.put("login", "yhduLhgogin");
        requestBody.put("firstName", "Oleg");
        ValidatableResponse errorResponse = client.sendRequestWithoutPasswordField(requestBody);

        // Проверяем, что возвращается ожидаемая ошибка
        errorResponse
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");
        id = loginResponse.extract().jsonPath().getString("id");

    }
    @After
    public void cleanUp() {
        if (id != null && !id.isEmpty()) { // Проверить, что id был получен
            client.deleteCourierById(id);

        }
    }
}
