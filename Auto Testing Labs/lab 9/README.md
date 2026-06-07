# Lab 9 — API Testing (JSONPlaceholder)

## Task
Test the [JSONPlaceholder](https://jsonplaceholder.typicode.com/) REST API using Java with layered architecture.

## Selected Endpoints
1. **`/posts`** — CRUD operations on posts
2. **`/comments`** — querying comments (by postId, nested route)

## Architecture (3 Layers)
```
src/test/java/com/stv/api/
├── core/                 # Core level: base test, specs
│   └── ApiBaseTest.java  # RestAssured setup, request/response specs
├── domain/               # Domain level: POJOs, test data
│   ├── Post.java         # Post POJO
│   ├── Comment.java      # Comment POJO
│   └── TestData.java     # Constants and test data
└── tests/                # Test level: actual test cases
    ├── SmokeTests.java   # Critical path (smoke) tests
    ├── PostTests.java    # Posts endpoint: CRUD + negative
    └── CommentTests.java # Comments endpoint: filter + negative
```

## Test Coverage

### Posts (CRUD)
| Test | Type | Description |
|---|---|---|
| GET all posts | Positive | 200, list non-empty, >= 100 posts |
| GET post by ID | Positive | 200, correct id & title |
| GET non-existent ID | Negative | 404 |
| POST create post | Positive | 201, returns created post |
| POST empty body | Negative | 201 (API is lenient) |
| PUT update post | Positive | 200, updated title |
| DELETE post | Positive | 200 |

### Comments
| Test | Type | Description |
|---|---|---|
| GET all comments | Positive | 200, list non-empty, >= 500 |
| GET by postId | Positive | 200, all have matching postId |
| GET non-existent postId | Negative | 200, empty list |
| GET invalid param | Negative | 200 (API is lenient) |
| GET nested route | Positive | 200, /posts/{id}/comments |

### Smoke Tests
- GET /posts, /comments, /users — all return 200
- Response time < 5 seconds

## How to Run
```bash
cd "Auto Testing Labs/lab 9"
mvn clean test
```

**Requirements:** Java 25, Maven 3.9+
