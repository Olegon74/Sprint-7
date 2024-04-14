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

    @After
    public void cleanUp() {
        if (id != null && !id.isEmpty()) { // Проверить, что id был получен
            client.deleteCourierById(id);

        }
    }

    @Test
    @DisplayName("The courier can log in with all required fields")
    @Description("Курьер может авторизоваться, передав все обязательные поля, позитивный тест")
    public void theCourierCanLogInWithAllRequiredFields() {


        Courier courier = Courier.create("testfrghyui", "12345", "Oleg");
        ValidatableResponse response = client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("Request with an incorrect login and password")
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку, негативный тест")
    public void requestWithAnIncorrectLoginAndPassword() {

        Credentials wrongCredentials = Credentials.withWrongCredentials("vcvcggfgfaa", "89898783434");
        ValidatableResponse loginResponse = client.login(wrongCredentials);
        loginResponse
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Request with an incorrect login")
    @Description("Cистема вернёт ошибку, если неправильно указать логин, негативный тест")
    public void requestWithAnIncorrectLogin() {

        Courier courier = Courier.create("testfrgsss", "12345", "Oleg");
        ValidatableResponse response = client.createCourier(courier);

        Credentials wrongCredentials = Credentials.withWrongCredentials("dfdfdfdfd", "12345");
        ValidatableResponse loginResponse = client.login(wrongCredentials);

        loginResponse
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginResponse2 = client.login(Credentials.fromCourier(courier));
        int courierId = loginResponse2.extract().jsonPath().getInt("id");
        if (courierId > 0) {
            client.deleteCourierById(Integer.toString(courierId));
        }

    }

    @Test
    @DisplayName("Request with an incorrect password")
    @Description("Cистема вернёт ошибку, если неправильно указать пароль, негативный тест")
    public void requestWithAnIncorrectPassword() {

        Courier courier = Courier.create("teswdsdf", "12345", "Oleg");
        ValidatableResponse response = client.createCourier(courier);

        Credentials wrongCredentials = Credentials.withWrongCredentials("teswdsdf", "54321");
        ValidatableResponse loginResponse = client.login(wrongCredentials);

        loginResponse
                .assertThat()
                .statusCode(404) // или другой код ошибки, который ожидается
                .body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginResponse2 = client.login(Credentials.fromCourier(courier));
        int courierId = loginResponse2.extract().jsonPath().getInt("id");
        if (courierId > 0) {
            client.deleteCourierById(Integer.toString(courierId));
        }

    }

    @Test
    @DisplayName("Successful login returns an ID")
    @Description("При успешной авторизации возвращается ID курьера")
    public void successfulLoginReturnsId() {
        // Создаем нового курьера
        Courier courier = Courier.create("testLoginn", "testPassword", "Oleg");
        ValidatableResponse createCourierResponse = client.createCourier(courier);

        // Авторизуемся с созданным курьером
        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));


        int courierId = loginResponse.extract().jsonPath().getInt("id");

        // Проверяем, что ID возвращается и является положительным числом
        assertTrue("Courier ID should be a positive number", courierId > 0);

        // Удаляем созданного курьера, если тест завершился успешно
        if (courierId > 0) {
            client.deleteCourierById(Integer.toString(courierId));
        }

    }

    @Test
    @DisplayName("Request without login field returns an error")
    @Description("Тест проверяет, что при отсутствии поля login в запросе возвращается ошибка")
    public void requestWithoutLoginFieldReturnsError() {
        // Создаем курьера для проверки
        Courier courier = Courier.create("testWithoutLogin", "12345", "Oleg");
        ValidatableResponse createCourierResponse = client.createCourier(courier);

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

        ValidatableResponse loginResponse2 = client.login(Credentials.fromCourier(courier));
        int courierId = loginResponse2.extract().jsonPath().getInt("id");
        if (courierId > 0) {
            client.deleteCourierById(Integer.toString(courierId));
        }

    }

    @Test

    @DisplayName("Request without password field returns an error")
    @Description("Тест проверяет, что при отсутствии поля password в запросе возвращается ошибка")
    @Ignore("Причина игнорирования этого теста")
    public void requestWithoutPasswordFieldReturnsError() {
        // Создаем курьера для проверки
        Courier courier = Courier.create("testdsfdfrt", "12343", "Petya");
        ValidatableResponse createCourierResponse = client.createCourier(courier);

        // Отправляем запрос без поля password
        Map<String, String>requestBodys = new HashMap<>();
        requestBodys.put("login", "testdsdsd");
        requestBodys.put("firstName", "Oleg");
        ValidatableResponse errorResponse = client.sendRequestWithoutPasswordField(requestBodys);

        // Проверяем, что возвращается ожидаемая ошибка
        errorResponse
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));

        ValidatableResponse loginResponse2 = client.login(Credentials.fromCourier(courier));
        int courierId = loginResponse2.extract().jsonPath().getInt("id");
        if (courierId > 0) {
            client.deleteCourierById(Integer.toString(courierId));
        }
    }
}
