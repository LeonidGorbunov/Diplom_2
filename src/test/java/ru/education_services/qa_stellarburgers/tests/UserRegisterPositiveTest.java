package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers")
@Feature("1. Регистрация нового пользователя, эндпоинт post /api/auth/register")
@Story("1. Позитивный сценарий")
public class UserRegisterPositiveTest extends BaseTest {

    private UserRegisterRequest userRequest;
    private String accessToken;

    @Before
    public void prepareTestData() {
        userRequest = UserDataGenerator.getDefaultUser();
    }

    @Test
    @DisplayName("1. Успешная регистрация нового пользователя")
    @Description("Позитивный сценарий регистрации нового пользователя. Проверка статус-кода 200 и валидация JSON-контракта " +
            "успешного ответа.")
    public void shouldRegisterUserWhenDataIsValid () {

        Response response = userClient.actionUserRegister(userRequest);

        accessToken = response.path("accessToken");

        Allure.step("Проверка контракта и тела успешного ответа", () -> {
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("success", describedAs("Флаг \"success\" в ответе не true!", is(true)))
                    .body("accessToken", describedAs("Поле \"accessToken\" отсутствует в ответе или равно null!", notNullValue()))
                    .body("refreshToken", describedAs("Поле \"refreshToken\" отсутствует в ответе или равно null!", notNullValue()))
                    .body("user.email", describedAs("Поле email в ответе не совпадает с отправленным!", equalTo(userRequest.getEmail())))
                    .body("user.name", describedAs("Поле name в ответе не совпадает с отправленным!", equalTo(userRequest.getName())));
        });
    }

    @After
    public void tearDown() {
        apiUserTeardownSteps.deleteUserSafely(accessToken);
    }
}
