package org.example.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.qameta.allure.restassured.AllureRestAssured;
import org.example.config.TestConfig;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;

public class ApiUtils {
    private static final TestConfig config = TestConfig.getInstance();
    private static final RequestSpecification requestSpec;

    static {

        RestAssured.baseURI = config.getBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    public static Response get(String endpoint){
        return given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


    public static Response post(String endpoint, Object body, Object... pathParams) {
        return given()
                .spec(requestSpec)
                .pathParams("editor", pathParams[0])
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response put(String endpoint, Object body, Object... pathParams) {
        return given()
                .spec(requestSpec)
                .pathParam("editor", pathParams[0])
                .pathParam("id", pathParams[1])
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response delete(String endpoint, Object body, Object... pathParams) {
        return given()
                .spec(requestSpec)
                .pathParam("editor", pathParams[0])
                .body(body)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}
