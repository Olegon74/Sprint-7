package orders;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

public class OrderClientImpl implements OrderClient {
    public final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_ORDER_ENDPOINT = "api/v1/orders";
    public static final String LIST_OF_ORDERS_ENDPOINT = "api/v1/orders?courierId=";


    @Override
    public ValidatableResponse createOrders(Orders orders) {
        return RestAssured.given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(orders)
                .post(CREATE_ORDER_ENDPOINT)
                .then()
                .log()
                .all();

    }
}
