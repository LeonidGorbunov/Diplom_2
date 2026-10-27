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
            assertEquals("Статус-код ответа не 200!", 200, response.getStatusCode());
            assertTrue("Флаг \"success\" в ответе не true!", response.path("success"));
            assertNotNull("Поле \"accessToken\" отсутствует в ответе или равно null!", accessToken);
            assertNotNull("Поле \"refreshToken\" отсутствует в ответе или равно null!", response.path("refreshToken"));
            assertEquals("Поле email в ответе не совпадает с отправленным!", userRequest.getEmail(), response.path("user.email"));
            assertEquals("Поле name в ответе не совпадает с отправленным!", userRequest.getName(), response.path("user.name"));
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
    }
}
