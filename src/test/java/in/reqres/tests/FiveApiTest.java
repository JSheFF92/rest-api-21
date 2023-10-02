package in.reqres.tests;


import in.reqres.models.*;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.Specs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FiveApiTest {

    @Test
    void getFirstAndLastNameAndIdTest() {
        DataModel data = step("Получаем массив пользователя", () ->
                given(request)
                        .get("/users/2")
                        .then()
                        .spec(responseSpecWithStatusCode200)
                        .extract().as(DataModel.class));

        step("Проверяем Id, First Name, Last Name", () -> {
            assertEquals(2, data.getUser().getId());
            assertEquals("Janet", data.getUser().getFirstName());
            assertEquals("Weaver", data.getUser().getLastName());
        });
    }

    @Test
    void listUsersTest() {
        GetListUsersModel responseUsers = step("Получаем массив пользователей", () ->
                given(request)
                        .get("/users?page=2")
                        .then()
                        .spec(responseSpecWithStatusCode200)
                        .extract().as(GetListUsersModel.class));

        step("Проверяем данные в массиве", () -> {
            List<ListUsersDataResponseModel> data = responseUsers.getData();
            assertEquals("Byron", data.get(3).getFirstName());
            assertEquals("Fields", data.get(3).getLastName());
            assertEquals(9, responseUsers.getData().get(2).getId());
            assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!",
                    responseUsers.getSupport().getText());
            assertEquals(6, responseUsers.getPerPage());
        });

    }

    @Test
    void createUserTest() {
        BodyUserModel body = new BodyUserModel();
        body.setName("Jenya");
        body.setJob("QA");

        CreateResponseModel responseNewUser = step("Создаем пользователя с заданными Name и Job", () ->
                given(request)
                        .body(body)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseSpecWithStatusCode201)
                        .extract().as(CreateResponseModel.class));

        step("Проверяем созданного пользователя с заданными параметрами", () -> {
            assertEquals("Jenya", responseNewUser.getName());
            assertEquals("QA", responseNewUser.getJob());
            assertNotNull(responseNewUser.getId());
            assertNotNull(responseNewUser.getCreatedAt());
        });
    }

    @Test
    void putUserTest() {
        BodyUserModel updateBody = new BodyUserModel();
        updateBody.setName("SuperJenya");
        updateBody.setJob("MEGAULTRAQA");
        PutResponseUserModel responseUpdateUser = step("Обновляем Name и Job пользователю", () ->
                given(request)
                        .body(updateBody)
                        .put("/users/2/")
                        .then()
                        .spec(responseSpecWithStatusCode200)
                        .extract().as(PutResponseUserModel.class));

        step("Проверяем изменения", () -> {
            assertEquals("SuperJenya", responseUpdateUser.getName());
            assertEquals("MEGAULTRAQA", responseUpdateUser.getJob());
            assertNotNull(responseUpdateUser.getUpdatedAt());
        });
    }

    @Test
    void deleteUserTest() {
        step("Проверяем статус удаления пользователя", () -> {
                given(request)
                        .delete("/users/2")
                        .then()
                        .log().body()
                        .spec(responseSpecWithStatusCode204);
        });
    }
}