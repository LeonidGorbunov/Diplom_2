package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers")
@Feature("1. Регистрация нового пользователя, эндпоинт post /api/auth/register")
@Story("3. Негативный сценарий регистрации дубля пользователя")
public class UserRegisterNegativeTest extends BaseTest {

    private UserRegisterRequest userRequest;
    private String accessToken;
    private String accessTokenDuplicate;

    @Before
    public void prepareTestData() {
        userRequest = UserDataGenerator.getDefaultUser();
        Response response = userClient.actionUserRegister(userRequest);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("1. Регистрация полного дубля уже существующего пользователя")
    @Description("Негативный сценарий регистрации полного дубля существующего пользователя. Проверка статус-кода 403 " +
            "и валидация JSON-контракта ответа.")
    public void registerDuplicateUserReturns403Forbidden() {

        Response duplicateResponse = userClient.actionUserRegister(userRequest);
        accessTokenDuplicate = duplicateResponse.path("accessToken");

        Allure.step("Проверка контракта и тела ответа", () -> {
            duplicateResponse.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("success", describedAs("Флаг \"success\" в ответе не false!", is(false)))
                    .body("message", describedAs("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                            equalTo("User already exists")));
        });
    }

    @After
    public void tearDown() {
        apiUserTeardownSteps.deleteUserSafely(accessToken);
        apiUserTeardownSteps.deleteUserSafely(accessTokenDuplicate);
    }
}