package org.example.api.client;

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

public class DefaultRestClient implements RestClient{

    private final RequestSpecification requestSpec;

    public DefaultRestClient(TestConfig config) {
        RestAssured.baseURI = config.getBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Override
    public Response get(String endpoint, Object... pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec);
        applyPathParams(request, pathParams);
        return request.when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response post(String endpoint, Object body, Object... pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec);
        if (body != null) {
            request.body(body);
        }
        applyPathParams(request, pathParams);
        return request.when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response put(String endpoint, Object body, Object... pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec);
        if (body != null) {
            request.body(body);
        }
        applyPathParams(request, pathParams);
        return request.when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    @Override
    public Response delete(String endpoint, Object... pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec);
        applyPathParams(request, pathParams);
        return request.when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    private void applyPathParams(RequestSpecification request, Object... pathParams) {
        if (pathParams != null && pathParams.length > 0) {
            for (int i = 0; i < pathParams.length; i += 2) {
                if (i + 1 < pathParams.length) {
                    request.pathParam(pathParams[i].toString(), pathParams[i + 1]);
                }
            }
        }
    }
}
