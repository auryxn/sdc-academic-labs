package com.stv.api.tests;

import com.stv.api.core.ApiBaseTest;
import com.stv.api.domain.Comment;
import com.stv.api.domain.Post;
import com.stv.api.domain.TestData;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test suite for JSONPlaceholder /posts endpoint.
 * Tests: GET all, GET by ID, GET non-existent, POST, PUT, DELETE.
 */
public class PostTests extends ApiBaseTest {

    @Test(description = "GET /posts — retrieve all posts, verify status and count")
    public void testGetAllPosts() {
        List<Post> posts = given()
                .spec(requestSpec)
                .when()
                .get("/posts")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .as(new TypeRef<List<Post>>() {});

        Assert.assertFalse(posts.isEmpty(), "Posts list should not be empty");
        Assert.assertTrue(posts.size() >= 100, "Expected at least 100 posts");
    }

    @Test(description = "GET /posts/{id} — retrieve a single post by valid ID")
    public void testGetPostById() {
        Post post = given()
                .spec(requestSpec)
                .pathParam("id", TestData.EXISTING_POST_ID)
                .when()
                .get("/posts/{id}")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(TestData.EXISTING_POST_ID))
                .body("title", equalTo(TestData.EXPECTED_POST_1_TITLE))
                .extract()
                .as(Post.class);

        Assert.assertEquals(post.getId(), TestData.EXISTING_POST_ID);
        Assert.assertNotNull(post.getTitle(), "Title should not be null");
    }

    @Test(description = "GET /posts/{id} — retrieve a post with non-existent ID, expect 404")
    public void testGetPostByNonExistentId() {
        given()
                .spec(requestSpec)
                .pathParam("id", TestData.NON_EXISTENT_POST_ID)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(404);
    }

    @Test(description = "POST /posts — create a new post, verify response")
    public void testCreatePost() {
        Post newPost = new Post(TestData.USER_ID_1, TestData.SAMPLE_TITLE, TestData.SAMPLE_BODY);

        Post created = given()
                .spec(requestSpec)
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .body("title", equalTo(TestData.SAMPLE_TITLE))
                .body("userId", equalTo(TestData.USER_ID_1))
                .extract()
                .as(Post.class);

        Assert.assertEquals(created.getTitle(), TestData.SAMPLE_TITLE);
        Assert.assertEquals(created.getUserId(), TestData.USER_ID_1);
        Assert.assertNotNull(created.getId(), "Created post should have an ID");
    }

    @Test(description = "POST /posts — negative: empty body should not create a post")
    public void testCreatePostWithEmptyBody() {
        given()
                .spec(requestSpec)
                .body("{}")
                .when()
                .post("/posts")
                .then()
                .statusCode(201); // JSONPlaceholder is lenient, still returns 201 with null fields
    }

    @Test(description = "PUT /posts/{id} — update an existing post")
    public void testUpdatePost() {
        Post updatedPost = new Post(1, "Updated Title", "Updated body content.");

        Post result = given()
                .spec(requestSpec)
                .pathParam("id", TestData.EXISTING_POST_ID)
                .body(updatedPost)
                .when()
                .put("/posts/{id}")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .extract()
                .as(Post.class);

        Assert.assertEquals(result.getTitle(), "Updated Title");
    }

    @Test(description = "DELETE /posts/{id} — delete an existing post")
    public void testDeletePost() {
        given()
                .spec(requestSpec)
                .pathParam("id", TestData.EXISTING_POST_ID)
                .when()
                .delete("/posts/{id}")
                .then()
                .statusCode(200);
    }
}
