package ru.education_services.qa_stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.education_services.qa_stellarburgers.models.request.UserLoginRequest;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Действие: регистрация нового пользователя, post /api/auth/register")
    public Response actionUserRegister (UserRegisterRequest userRequest) {

        return given(getBaseSpec())
                .body(userRequest)
                .when()
                .post("/api/auth/register");
    }

    @Step("Действие: удаление пользователя, delete /api/auth/user")
    public Response actionUserDelete (String accessToken) {

        return given(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }

    @Step("Действие: авторизация пользователя, post /api/auth/login")
    public Response actionUserLogin (UserLoginRequest loginRequest) {

        return given(getBaseSpec())
                .body(loginRequest)
                .when()
                .post("/api/auth/login");
    }
}