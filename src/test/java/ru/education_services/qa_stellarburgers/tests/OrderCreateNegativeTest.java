package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.IngredientDataGenerator;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.OrderCreateRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@Epic("Stellar Burgers")
@Feature("3. Создание заказа, эндпоинт post /api/orders")
@Story("2. Негативные сценарии")
public class OrderCreateNegativeTest extends BaseTest {

    private String accessToken;
    private static final String INVALID_HASH_FORMAT = "invalid_hashformat_in_id";

    @Before
    public void prepareTestData() {
        Response registerResponse = userClient.actionUserRegister(UserDataGenerator.getDefaultUser());
        accessToken = registerResponse.path("accessToken");
    }

    @Test
    @DisplayName("1. Создание заказа с ингредиентами неавторизованным пользователем")
    @Description("Негативный сценарий создания заказа с ингредиентами неавторизованным пользователем. Проверка " +
            "статус-кода 401 и валидация JSON-контракта ответа.")
    public void shouldReturn401UnauthorizedWhenOrderCreatedWithoutToken () {

        List<String> ingredients = orderClient.actionGetIngredients().path("data._id");
        OrderCreateRequest validOrderRequest = new OrderCreateRequest(Arrays.asList(ingredients.get(0), ingredients.get(1)));

        Response response = orderClient.actionOrderCreate(validOrderRequest, null);

        Allure.step("Проверка контракта и тела ответа", () -> {
            assertEquals("Статус-код ответа не 401!", 401, response.getStatusCode());
            assertFalse("Флаг \"success\" в ответе не false!", response.path("success"));
            assertEquals("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                    "You should be authorized", response.path("message"));
        });
    }

    @Test
    @DisplayName("2. Создание заказа без ингредиентов авторизованным пользователем")
    @Description("Негативный сценарий создания заказа без ингредиентов авторизованным пользователем. Проверка " +
            "статус-кода 400 и валидация JSON-контракта ответа.")
    public void shouldReturn400BadRequestWhenOrderCreatedWithoutIngredients () {

        OrderCreateRequest emptyOrderRequest = new OrderCreateRequest(Collections.emptyList());

        Response response = orderClient.actionOrderCreate(emptyOrderRequest, accessToken);

        Allure.step("Проверка контракта и тела ответа", () -> {
            assertEquals("Статус-код ответа не 400!", 400, response.getStatusCode());
            assertFalse("Флаг \"success\" в ответе не false!", response.path("success"));
            assertEquals("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                    "Ingredient ids must be provided", response.path("message"));
        });
    }

    @Test
    @DisplayName("3. Создание заказа с неверным хэшем ингредиентов авторизованным пользователем")
    @Description("Негативный сценарий создания заказа с неверным хэшем (несуществующим в БД) ингредиентов авторизованным " +
            "пользователем. Проверка статус-кода 400 и валидация JSON-контракта ответа.")
    public void shouldReturn400BadRequestWhenOrderCreatedWithWrongHashOfIngredients () {

        List<String> ingredients = orderClient.actionGetIngredients().path("data._id");
        List<String> nonExistentIngredients = IngredientDataGenerator.getNonExistentIngredients(ingredients);
        OrderCreateRequest orderRequestWithNonExistentIngredients = new OrderCreateRequest(nonExistentIngredients);

        Response response = orderClient.actionOrderCreate(orderRequestWithNonExistentIngredients, accessToken);

        Allure.step("Проверка контракта и тела ответа", () -> {
            assertEquals("Статус-код ответа не 400!", 400, response.getStatusCode());
            assertFalse("Флаг \"success\" в ответе не false!", response.path("success"));
            assertEquals("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                    "One or more ids provided are incorrect", response.path("message"));
        });
    }

    @Test
    @DisplayName("4. Создание заказа с невалидным форматом хэша ингредиентов авторизованным пользователем")
    @Description("Негативный сценарий создания заказа авторизованным пользователем с передчей строки произвольного " +
            "формата вместо валидного Hex-хэша. Проверка статус-кода 400 и валидация JSON-контракта ответа.")
    public void shouldReturn400BadRequestWhenOrderCreatedWithInvalidIdFormat() {

        OrderCreateRequest invalidFormatRequest = new OrderCreateRequest(List.of(INVALID_HASH_FORMAT));

        Response response = orderClient.actionOrderCreate(invalidFormatRequest, accessToken);

        Allure.step("Проверка контракта и тела ответа", () -> {
            assertEquals("Статус-код ответа не 400!", 400, response.getStatusCode());
            assertFalse("Флаг \"success\" в ответе не false!", response.path("success"));
            assertEquals("Текст поля \"message\" в ответе не совпадает с ожидаемым!",
                    "One or more ids provided are incorrect", response.path("message"));
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