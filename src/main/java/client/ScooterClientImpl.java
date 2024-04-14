package client;

import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ScooterClientImpl implements ScooterClient {

    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_USER_ENDPOINT = "api/v1/courier";
    public static final String LOGIN_ENDPOINT = "api/v1/courier/login";
    public static final String DELETE_EDPOINT = "api/v1/courier/:id";

    @Override
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(courier)
                .post(CREATE_USER_ENDPOINT)
                .then()
                .log()
                .all();

    }

    @Override
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(credentials)
                .post(LOGIN_ENDPOINT)
                .then()
                .log()
                .all();
    }

    @Override
    public ValidatableResponse deleteCourierById(String id) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .pathParam("id", id)
                .delete(DELETE_EDPOINT.replace(":id", "{id}"))
                .then()
                .log()
                .all();
    }

    @Override
    public ValidatableResponse createCourierUsingMap(Map<String, String> courierData) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(courierData)
                .post(CREATE_USER_ENDPOINT)
                .then()
                .log().all();
    }

    public ValidatableResponse sendRequestWithoutLoginField(Map<String, String> requestBody) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(requestBody)
                .post(LOGIN_ENDPOINT)
                .then()
                .log().all();

    }

    public ValidatableResponse sendRequestWithoutPasswordField(Map<String, String> requestBodys) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(requestBodys)
                .post(LOGIN_ENDPOINT)
                .then()
                .log().all();

    }
}
