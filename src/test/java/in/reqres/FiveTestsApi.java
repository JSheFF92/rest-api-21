package in.reqres;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class FiveTestsApi extends TestBase{

    @Test
    void getStatus200AndIdTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
//                .get("https://reqres.in/api/users/2")
                .get(baseURI + basePath + "/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2));
    }

    @Test
    void listUsersTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("support.text",
                        is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void createUserTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{ \"name\": \"Jenya\", \"job\": \"QA\" }")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);
    }

    @Test
    void putUserTest() {
        String id = given()
//                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{ \"name\": \"Jenya\", \"job\": \"QA\" }")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
//                .log().body()
                .statusCode(201)
                .extract().path("id");

        given().log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{ \"name\": \"SuperJenya\", \"job\": \"MEGAULTRAQA\" }")
                .put("https://reqres.in/api/users/2/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("SuperJenya"))
                .body("job", is("MEGAULTRAQA"));
    }

    @Test
    void deleteUserTest() {
        String id = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{ \"name\": \"Jenya\", \"job\": \"QA\" }")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().path("id");

        given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/2" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}