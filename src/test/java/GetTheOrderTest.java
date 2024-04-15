import client.Courier;
import client.Credentials;
import client.ScooterClient;
import client.ScooterClientImpl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static client.ScooterClientImpl.BASE_URI;
import static io.restassured.RestAssured.given;
import static orders.OrderClientImpl.LIST_OF_ORDERS_ENDPOINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetTheOrderTest {
    private String id;


    @Test
    @DisplayName("Get the order")
    @Description("Получение списка заказов")
    public void getTheOrder() {
        ScooterClientImpl client = new ScooterClientImpl();
        Courier courier = Courier.create("tfdваfesdfi", "12345", "Vasya");
        client.createCourier(courier);
        ValidatableResponse loginResponse = client.login(Credentials.fromCourier(courier));
        String courierId = loginResponse.extract().jsonPath().getString("id");

        ValidatableResponse ordersResponse = given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .get(LIST_OF_ORDERS_ENDPOINT + courierId)
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

        if (id != null && !id.isEmpty()) {
            client.deleteCourierById(id);

        }
    }
}
