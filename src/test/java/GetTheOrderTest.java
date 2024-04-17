import client.Courier;
import client.Credentials;
import client.ScooterClientImpl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static client.ScooterClientImpl.BASE_URI;
import static io.restassured.RestAssured.given;
import static orders.OrderClientImpl.LIST_OF_ORDERS_ENDPOINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetTheOrderTest {
    private String id;
    private ScooterClientImpl client;

    @Before
    public void setUp() {
        client = new ScooterClientImpl();
    }


    @Test
    @DisplayName("Get the order")
    @Description("Получение списка заказов")
    public void getTheOrder() {

        client = new ScooterClientImpl();
        Courier courier = Courier.create("sdngfbnfi", "12345", "Vasya");
        client.createCourier(courier);
        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        loginResponse.extract().jsonPath().getString("id");

        id = loginResponse.extract().jsonPath().getString("id");

        ValidatableResponse ordersResponse = given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .get(LIST_OF_ORDERS_ENDPOINT + id)
                .then()
                .log()
                .all();


        ordersResponse
                .statusCode(200)
                .body("orders.size()", equalTo(0)) // Проверка, что массив заказов пуст
                .body("pageInfo.page", equalTo(0))
                .body("pageInfo.total", equalTo(0))
                .body("pageInfo.limit", equalTo(30))
                .body("availableStations", notNullValue());

    }

    @After
    public void cleanUp() {
        if (id != null && !id.isEmpty()) { // Проверить, что id был получен
            client.deleteCourierById(id);

        }

    }

}

