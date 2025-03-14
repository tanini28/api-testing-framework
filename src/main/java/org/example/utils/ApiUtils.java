package org.example.utils;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.config.TestConfig;


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

    public static Response get(String endpoint) {
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response post(String endpoint, Object body, Object... pathParams) {
        var request = RestAssured.given().spec(requestSpec).body(body);

        if (pathParams.length > 0) {
            request.pathParam("editor", String.valueOf(pathParams[0]));
        }

        return request.when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response put(String endpoint, Object body, Object... pathParams) {
        var request = RestAssured.given().spec(requestSpec).body(body);

        if (pathParams.length > 0) {
            request.pathParam("editor", String.valueOf(pathParams[0]));
        }
        if (pathParams.length > 1) {
            request.pathParam("id", String.valueOf(pathParams[1]));
        }

        return request.when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response delete(String endpoint, Object... pathParams) {
        var request = RestAssured.given().spec(requestSpec);

        if (pathParams.length > 0) {
            request.pathParam("editor", String.valueOf(pathParams[0]));
        }
        if (pathParams.length > 1) {
            request.pathParam("id", String.valueOf(pathParams[1]));
        }

        return request.when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}