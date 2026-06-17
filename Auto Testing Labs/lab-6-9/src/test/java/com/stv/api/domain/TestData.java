package com.stv.api.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Helper constants and test data for JSONPlaceholder API tests.
 */
public class TestData {

    public static final int EXISTING_POST_ID = 1;
    public static final int NON_EXISTENT_POST_ID = 99999;
    public static final int USER_ID_1 = 1;
    public static final String SAMPLE_TITLE = "Test Title from Automation";
    public static final String SAMPLE_BODY = "Test body content for the new post.";

    public static final List<String> EXPECTED_POST_1_KEYS = Arrays.asList("userId", "id", "title", "body");
    public static final String EXPECTED_POST_1_TITLE = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";
}
