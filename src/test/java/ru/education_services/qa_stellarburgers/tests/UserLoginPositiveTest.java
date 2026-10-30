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
@Story("1. Позитивный сценарий")
public class UserLoginPositiveTest extends BaseTest {

    private UserRegisterRequest userRequest;
    private UserLoginRequest loginRequest;
    private String accessToken;

    @Before
    public void prepareTestData() {
        userRequest = UserDataGenerator.getDefaultUser();
        Response registerResponse = userClient.actionUserRegister(userRequest);
        accessToken = registerResponse.path("accessToken");
        loginRequest = new UserLoginRequest(userRequest.getEmail(), userRequest.getPassword());
    }

    @Test
    @DisplayName("1. Успешная авторизация пользователя")
    @Description("Позитивный сценарий авторизации пользователя. Проверка статус-кода 200 и валидация JSON-контракта успешного ответа.")
    public void shouldLoginUserWhenCredentialsAreValid () {

        Response response = userClient.actionUserLogin(loginRequest);

        Allure.step("Проверка контракта и тела успешного ответа", () -> {
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("success", describedAs("Флаг \"success\" в ответе не true!", is(true)))
                    .body("accessToken", describedAs("Поле \"accessToken\" отсутствует в ответе или равно null!", notNullValue()))
                    .body("refreshToken", describedAs("Поле \"refreshToken\" отсутствует в ответе или равно null!", notNullValue()))
                    .body("user.email", describedAs("Поле email в ответе не совпадает с отправленным!", equalTo(loginRequest.getEmail())))
                    .body("user.name", describedAs("Поле name в ответе не совпадает с отправленным при регистрации!", equalTo(userRequest.getName())));
        });
    }

    @After
    public void tearDown() {
        apiUserTeardownSteps.deleteUserSafely(accessToken);
    }
}
