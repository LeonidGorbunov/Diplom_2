package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers")
@Feature("1. Регистрация нового пользователя, эндпоинт post /api/auth/register")
@Story("2. Негативные сценарии (параметризованный тест)")
@RunWith(Parameterized.class)
public class UserRegisterParametrizedNegativeTest extends BaseTest {

    private String accessToken;

    @Parameterized.Parameter(0)
    public String fieldName;

    @Parameterized.Parameter(1)
    public String invalidValue;

    @Parameterized.Parameter(2)
    public String displayTestName;

    @Parameterized.Parameters(name = "{2}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"email", null, "Регистрация нового пользователя без передачи обязательного поля \"email\" (null)"},
                {"password", null, "Регистрация нового пользователя без передачи обязательного поля \"password\" (null)"},
                {"name", null, "Регистрация нового пользователя без передачи обязательного поля \"name\" (null)"},
                {"email", "", "Регистрация нового пользователя с пустым полем \"email\" (пустая строка)"},
                {"password", "", "Регистрация нового пользователя с пустым полем \"password\" (пустая строка)"},
                {"name", "", "Регистрация нового пользователя с пустым полем \"name\" (пустая строка)"}
        };
    }

    @Test
    @Description("Параметризованный тест. Проверяет статус-код ответа 403 и валидацию JSON-контракта ответа при " +
            "отсутствии любого обязательного поля или при передаче пустой строки в любом из обязательных полей.")
    public void shouldReturn403ForbiddenWhenRequiredFieldIsNullOrEmpty() {

        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName(displayTestName));
        Allure.parameter("Тестируемое поле", fieldName);
        Allure.parameter("Тестовое значение", invalidValue == null ? "null" : "\"\"");

        UserRegisterRequest userRequest = UserDataGenerator.getDefaultUser();

        switch (fieldName) {
            case "email": userRequest.setEmail(invalidValue);
            break;
            case "password": userRequest.setPassword(invalidValue);
            break;
            case "name": userRequest.setName(invalidValue);
            break;
        }

        Response response = userClient.actionUserRegister(userRequest);

        accessToken = response.path("accessToken");

        Allure.step("Проверка контракта и тела ответа", () -> {
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("success", describedAs("Флаг \"success\" в ответе не false!", is(false)))
                    .body("message", describedAs("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                            equalTo("Email, password and name are required fields")));
        });
    }

    @After
    public void tearDown() {
        apiUserTeardownSteps.deleteUserSafely(accessToken);
    }
}