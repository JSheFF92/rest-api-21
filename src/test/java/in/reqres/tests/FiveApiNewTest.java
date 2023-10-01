package in.reqres.tests;


import in.reqres.models.*;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.Specs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FiveApiNewTest {

    @Test
    void getFirstAndLastNameAndIdTest() {
        OneTestUserData data = step("Получаем массив пользователя", () ->
                given(request)
                        .get("/users/2")
                        .then()
                        .spec(response)
                        .extract().as(OneTestUserData.class));

        step("Проверяем Id, First Name, Last Name", () -> {
            assertEquals(2, data.getUser().getId());
            assertEquals("Janet", data.getUser().getFirstName());
            assertEquals("Weaver", data.getUser().getLastName());
        });
    }

    @Test
    void listUsersTest() {
        TwoTestGetListUsers responseUsers = step("Получаем массив пользователей", () ->
                given(request)
                        .get("/users?page=2")
                        .then()
                        .spec(response)
                        .extract().as(TwoTestGetListUsers.class));

        step("Проверяем данные в массиве", () -> {
            List<TwoTestListUsersDataResponseModel> data = responseUsers.getData();
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
        ThreeTestCreateBodyUserModel body = new ThreeTestCreateBodyUserModel();
        body.setName("Jenya");
        body.setJob("QA");

        ThreeTestCreateResponseModel responseNewUser = step("Создаем пользователя с заданными Name и Job", () ->
                given(request)
                        .body(body)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseCreate)
                        .extract().as(ThreeTestCreateResponseModel.class));

        step("Проверяем созданного пользователя с заданными параметрами", () -> {
            assertEquals("Jenya", responseNewUser.getName());
            assertEquals("QA", responseNewUser.getJob());
            assertNotNull(responseNewUser.getId());
            assertNotNull(responseNewUser.getCreatedAt());
        });
    }

    @Test
    void putUserTest() {
        ThreeTestCreateBodyUserModel body = new ThreeTestCreateBodyUserModel();
        body.setName("Jenya");
        body.setJob("QA");
        String id = step("Получаем айди созданного пользователя с заданными Name и Job", () ->
                given(request)
                        .body(body)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseCreate)
                        .extract().path("id"));

        FourTestPutBodyUserModel updateBody = new FourTestPutBodyUserModel();
        updateBody.setName("SuperJenya");
        updateBody.setJob("MEGAULTRAQA");
        FourTestPutResponseUserModel responseUpdateUser = step("Обновляем Name и Job пользователю", () ->
                given(request)
                        .body(updateBody)
                        .put("/users/2/" + id)
                        .then()
                        .spec(response)
                        .extract().as(FourTestPutResponseUserModel.class));

        step("Проверяем изменения", () -> {
            assertEquals("SuperJenya", responseUpdateUser.getName());
            assertEquals("MEGAULTRAQA", responseUpdateUser.getJob());
            assertNotNull(responseUpdateUser.getUpdatedAt());
        });
    }

    @Test
    void deleteUserTest() {
        ThreeTestCreateBodyUserModel body = new ThreeTestCreateBodyUserModel();
        body.setName("Jenya");
        body.setJob("QA");
        String id = step("Создаем пользователя с заданными Name и Job", () ->
                given(request)
                        .body(body)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseCreate)
                        .extract().path("id"));

        step("Проверяем статус удаления пользователя", () -> {
                given(request)
                        .delete("/users/2" + id)
                        .then()
                        .log().body()
                        .spec(responseDelete);
        });
    }
}