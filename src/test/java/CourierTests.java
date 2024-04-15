import client.Courier;
import client.Credentials;
import client.ScooterClient;
import client.ScooterClientImpl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierTests {
    private ScooterClient client;
    private String id;

    @Before
    public void setUp() {

        client = new ScooterClientImpl();
    }

    @Test
    @DisplayName("Create courier in progress")
    @Description("Курьера можно создать, позитвный тест")
    public void createCourierInProgress() {


        Courier courier = Courier.create("testfryyui", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("You can create a courier without filling in the name field")
    @Description("Курьера можно создать без заполнения поля имя, позитвный тест")
    public void youCanCreateACourierWithoutFillingInTheNameField() {


        Courier courier = Courier.create("testfryyui", "12345", "");
        ValidatableResponse response = client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");
    }

    @Test
    @DisplayName("The сorrect response сode")
    @Description("Запрос возвращает правильный код, позитивный тест")
    public void theСorrectResponseCode() {


        Courier courier = Courier.create("testfryyui", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");

        response.assertThat().statusCode(201);

    }

    @Test
    @DisplayName("A successful request returns ok: true")
    @Description("Успешный запрос возвращает ok: true, позитивный тест")
    public void aSuccessfulRequestReturnsOkTrue() {


        Courier courier = Courier.create("testfryyui", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courier);

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");

        response.assertThat().body("ok", equalTo(true));
    }


/*
    @After
    public void cleanUp() {
        if (id != null && !id.isEmpty()) { // Проверить, что id был получен
            client.deleteCourierById(id);

        }*/

    @Test
    @DisplayName("Creating two couriers impossible")
    @Description("Нельзя создать двух одинаковых курьеров, негативный тест")
    public void creatingTwoCouriersImpossible() {

        Courier courier = Courier.create("testTwoCourier", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courier);

        Courier secondCourier = Courier.create("testTwoCourier", "565656", "Vasya");
        ValidatableResponse response2 = client.createCourier(secondCourier);

        response2.assertThat().statusCode(409)
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");


    }

    @Test
    @DisplayName("Checking for the required login field")
    @Description("Чтобы создать курьера, нужно передать в ручку все обязательные поля, негативный тест")
    public void checkingForTheRequiredLoginField() {


        Courier courierWithoutLogin = Courier.create("", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courierWithoutLogin);
        response.assertThat().statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));


    }
    @Test
    @DisplayName("Checking for the required password field")
    @Description("Чтобы создать курьера, нужно передать в ручку все обязательные поля, негативный тест")
    public void CheckingForTheRequiredPasswordField() {


        Courier courierWithoutPassword = Courier.create("testUser", "", "Vasya");
        ValidatableResponse response = client.createCourier(courierWithoutPassword);
        response.assertThat().statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Attempt to create a courier without a login field")
    @Description("Если одного из полей нет, запрос возвращает ошибку, негативный тест")
    public void attemptToCreateACourierWithouALoginField () {


        Map<String, String> credentialsWithoutLogin = new HashMap<>();
        credentialsWithoutLogin.put("password", "1234");
        credentialsWithoutLogin.put("firstName", "TestName");


        ValidatableResponse response = client.createCourierUsingMap(credentialsWithoutLogin);

        response.assertThat().statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Attempt to create a courier without a password field")
    @Description("Если одного из полей нет, запрос возвращает ошибку, негативный тест")
    public void attemptToCreateACourierWithoutEnteringPassword() {


        Map<String, String> credentialsWithoutLogin = new HashMap<>();
        credentialsWithoutLogin.put("login", "fdfdfdfd");
        credentialsWithoutLogin.put("firstName", "TestName");


        ValidatableResponse response = client.createCourierUsingMap(credentialsWithoutLogin);

        response.assertThat().statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Сreate a user with a username that already exists")
    @Description("Если создать пользователя с логином, который уже есть, возвращается ошибка, негативный тест")
    public void createAUserWithAUsernameThatAlreadyExists() {


        Courier courier = Courier.create("bdgfdfggf", "12345", "Vasya");
        ValidatableResponse response = client.createCourier(courier);

        //courier = Courier.create("rrfdfggf", "565656", "Vanya");
        //client.createCourier(courier);
        Courier secondCourier = Courier.create("bdgfdfggf", "565656", "Petya");
        ValidatableResponse response2 = client.createCourier(secondCourier);

        response2.assertThat().statusCode(409)
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

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
