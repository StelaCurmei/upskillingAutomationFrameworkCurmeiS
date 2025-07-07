package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

// Centralized API class for RestAssured tests.

public final class BaseApi {
    // Read once from config
    private static final String BASE_URI = ConfigReader.getProperty("api.baseUrl");

    // Shared request specification (base URI + JSON)
    private static final RequestSpecification REQUEST_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URI)
                    .setContentType("application/json")
                    .build();

    // Capture all HTTP traffic here
    private static final ByteArrayOutputStream LOG_STREAM = new ByteArrayOutputStream();

    static {
        // Install logging filters
        PrintStream ps = new PrintStream(LOG_STREAM, true, StandardCharsets.UTF_8);
        RestAssured.filters(
                new RequestLoggingFilter(LogDetail.ALL, ps),
                new ResponseLoggingFilter(LogDetail.ALL, ps)
        );

        // Apply shared spec globally
        RestAssured.requestSpecification = REQUEST_SPEC;
    }

    private BaseApi() { /* prevent instantiation */ }

    // Returns the recorded request/response log and clears the buffer. This is called in @AfterStep hook.
    public static String pullLogs() {
        String logs = LOG_STREAM.toString(StandardCharsets.UTF_8);
        LOG_STREAM.reset();
        return logs;
    }

    public static Response get(String path) {
        return RestAssured
                .given()
                .spec(REQUEST_SPEC)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    public static Response post(String path, Object body) {
        return RestAssured
                .given()
                .spec(REQUEST_SPEC)
                .body(body)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    public static Response put(String path, Object body) {
        return RestAssured
                .given()
                .spec(REQUEST_SPEC)
                .body(body)
                .when()
                .put(path)
                .then()
                .extract()
                .response();
    }

    public static Response delete(String path) {
        return RestAssured
                .given()
                .spec(REQUEST_SPEC)
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    // Clears any RestAssured filters (use in your @After hook if needed).
    public static void clearFilters() {
        RestAssured.replaceFiltersWith(Collections.<Filter>emptyList());
    }
}
