package orders;

import io.restassured.response.ValidatableResponse;

public interface OrderClient {
    ValidatableResponse createOrders(Orders orders);
}
