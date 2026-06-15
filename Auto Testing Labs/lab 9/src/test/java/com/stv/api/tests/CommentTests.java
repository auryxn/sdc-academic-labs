package com.stv.api.tests;

import com.stv.api.core.ApiBaseTest;
import com.stv.api.domain.Comment;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test suite for JSONPlaceholder /comments endpoint.
 * Tests: GET all, GET by postId, GET with query params, negative cases.
 */
public class CommentTests extends ApiBaseTest {

    @Test(description = "GET /comments — retrieve all comments")
    public void testGetAllComments() {
        List<Comment> comments = given()
                .spec(requestSpec)
                .when()
                .get("/comments")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .as(new TypeRef<List<Comment>>() {});

        Assert.assertFalse(comments.isEmpty(), "Comments list should not be empty");
        Assert.assertTrue(comments.size() >= 500, "Expected at least 500 comments");
    }

    @Test(description = "GET /comments?postId={id} — filter comments by post ID")
    public void testGetCommentsByPostId() {
        int postId = 1;

        List<Comment> comments = given()
                .spec(requestSpec)
                .queryParam("postId", postId)
                .when()
                .get("/comments")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("everyItem.postId", equalTo(postId))
                .extract()
                .as(new TypeRef<List<Comment>>() {});

        for (Comment c : comments) {
            Assert.assertEquals(c.getPostId(), postId,
                    "All comments should belong to postId=" + postId);
        }
    }

    @Test(description = "GET /comments?postId={id} — non-existent postId returns empty list")
    public void testGetCommentsByNonExistentPostId() {
        List<Comment> comments = given()
                .spec(requestSpec)
                .queryParam("postId", 99999)
                .when()
                .get("/comments")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Comment>>() {});

        Assert.assertTrue(comments.isEmpty(), "Expected empty list for non-existent postId");
    }

    @Test(description = "GET /comments — negative: invalid parameter type returns 200 (JSONPlaceholder is lenient)")
    public void testGetCommentsWithInvalidParam() {
        given()
                .spec(requestSpec)
                .queryParam("postId", "abc")
                .when()
                .get("/comments")
                .then()
                .statusCode(200); // JSONPlaceholder doesn't validate type strictly
    }

    @Test(description = "GET /posts/{id}/comments — retrieve comments for a specific post via nested route")
    public void testGetCommentsByPostNestedRoute() {
        int postId = 1;

        List<Comment> comments = given()
                .spec(requestSpec)
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}/comments")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .as(new TypeRef<List<Comment>>() {});

        Assert.assertFalse(comments.isEmpty());
    }
}
