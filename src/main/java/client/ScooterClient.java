package client;

import io.restassured.response.ValidatableResponse;

import java.util.Map;

public interface ScooterClient {
    ValidatableResponse createCourier(Courier courier);

    ValidatableResponse login(Credentials credentials);

    ValidatableResponse deleteCourierById(String id);


    ValidatableResponse createCourierUsingMap(Map<String, String> courierData);

    ValidatableResponse sendRequestWithoutLoginField(Map<String, String> requestBody);

    ValidatableResponse sendRequestWithoutPasswordField(Map<String, String> requestBodys);


}
