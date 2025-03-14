package org.example.api.client;

import io.restassured.response.Response;

public interface RestClient {
    Response get(String endpoint, Object... pathParams);
    Response post(String endpoint, Object body, Object... pathParams);
    Response put(String endpoint, Object body, Object... pathParams);
    Response delete(String endpoint, Object... pathParams);
}
