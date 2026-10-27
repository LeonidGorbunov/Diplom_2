package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import static org.junit.Assert.*;

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
            assertEquals("Статус-код ответа не 403!", 403, duplicateResponse.getStatusCode());
            assertFalse("Флаг \"success\" в ответе не false!", duplicateResponse.path("success"));
            assertEquals("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                    "User already exists", duplicateResponse.path("message"));
        });
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            try {
                userClient.actionUserDelete(accessToken);
            } catch (Exception e) {
                System.out.println("Не удалось удалить пользователя после теста: " + e.getMessage());
            }
        }
        if (accessTokenDuplicate != null) {
            try {
                userClient.actionUserDelete(accessTokenDuplicate);
            } catch (Exception e) {
                System.out.println("Не удалось удалить дубликат пользователя после теста: " + e.getMessage());
            }
        }
    }
}
