package com.stv.api.tests;

import com.stv.api.core.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Critical Path (Smoke) tests for JSONPlaceholder API.
 * Verifies that the core endpoints are up and responding.
 */
public class SmokeTests extends ApiBaseTest {

    @Test(description = "SMOKE: GET /posts returns 200 with non-empty body")
    public void smokePostsEndpoint() {
        given()
                .spec(requestSpec)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(description = "SMOKE: GET /comments returns 200 with non-empty body")
    public void smokeCommentsEndpoint() {
        given()
                .spec(requestSpec)
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(description = "SMOKE: GET /users returns 200 with non-empty body")
    public void smokeUsersEndpoint() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(description = "SMOKE: Response time is acceptable (< 5 seconds)")
    public void smokeResponseTime() {
        given()
                .spec(requestSpec)
                .when()
                .get("/posts")
                .then()
                .time(lessThan(5000L));
    }
}
