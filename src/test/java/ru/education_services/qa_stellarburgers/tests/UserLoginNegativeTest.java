package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.UserLoginRequest;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers")
@Feature("2. Авторизация пользователя, эндпоинт post /api/auth/login")
@Story("2. Негативные сценарии")
public class UserLoginNegativeTest extends BaseTest {

    private UserRegisterRequest userRequest;
    private UserLoginRequest loginRequest;
    private String accessToken;

    @Before
    public void prepareTestData() {
        userRequest = UserDataGenerator.getDefaultUser();
        Response registerResponse = userClient.actionUserRegister(userRequest);
        accessToken = registerResponse.path("accessToken");
    }

    @Test
    @DisplayName("1. Авторизация пользователя с неверным логином (поле \"email\")")
    @Description("Негативный сценарий авторизации пользователя с неверным логином. Проверка статус-кода 401 и валидация JSON-контракта ответа.")
    public void loginUserWithWrongEmailReturns401Unauthorized() {

        loginRequest = new UserLoginRequest("fake_" + userRequest.getEmail(), userRequest.getPassword());

        Response response = userClient.actionUserLogin(loginRequest);

        Allure.step("Проверка контракта и тела ответа", () -> {
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .body("success", describedAs("Флаг \"success\" в ответе не false!", is(false)))
                    .body("message", describedAs("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                            equalTo("email or password are incorrect")));
        });
    }

    @Test
    @DisplayName("2. Авторизация пользователя с неверным паролем (поле \"password\")")
    @Description("Негативный сценарий авторизации пользователя с неверным паролем. Проверка статус-кода 401 и валидация JSON-контракта ответа.")
    public void loginUserWithWrongPasswordReturns401Unauthorized() {

        loginRequest = new UserLoginRequest(userRequest.getEmail(), userRequest.getPassword() + "_wrong");

        Response response = userClient.actionUserLogin(loginRequest);

        Allure.step("Проверка контракта и тела ответа", () -> {
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .body("success", describedAs("Флаг \"success\" в ответе не false!", is(false)))
                    .body("message", describedAs("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                            equalTo("email or password are incorrect")));
        });
    }

    @After
    public void tearDown() {
        apiUserTeardownSteps.deleteUserSafely(accessToken);
    }
}
