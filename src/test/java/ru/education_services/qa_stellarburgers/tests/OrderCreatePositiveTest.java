package ru.education_services.qa_stellarburgers.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.education_services.qa_stellarburgers.data.UserDataGenerator;
import ru.education_services.qa_stellarburgers.models.request.OrderCreateRequest;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

@Epic("Stellar Burgers")
@Feature("3. Создание заказа, эндпоинт post /api/orders")
@Story("1. Позитивный сценарий")
public class OrderCreatePositiveTest extends BaseTest {

    private OrderCreateRequest orderRequest;
    private String accessToken;

    @Before
    public void prepareTestData() {
        Response registerResponse = userClient.actionUserRegister(UserDataGenerator.getDefaultUser());
        accessToken = registerResponse.path("accessToken");

        List<String> ingredients = orderClient.actionGetIngredients().path("data._id");
        orderRequest = new OrderCreateRequest(Arrays.asList(ingredients.get(0), ingredients.get(1)));
    }

    @Test
    @DisplayName("1. Успешное создание заказа")
    @Description("Позитивный сценарий создания заказа с ингредиентами авторизованным пользователем. Проверка статус-кода " +
            "200 и валидация JSON-контракта успешного ответа.")
    public void shouldCreateOrderWhenUserIsAuthorizedWithValidIngredients() {

        Response response = orderClient.actionOrderCreate(orderRequest, accessToken);

        Allure.step("Проверка контракта и тела успешного ответа", () -> {
            assertEquals("Статус-код ответа не 200!", 200, response.getStatusCode());
            assertTrue("Флаг \"success\" в ответе не true!", response.path("success"));
            assertTrue("Поле \"order.number\" в ответе отсутствует или не является целым числом!", response.path("order.number") instanceof Integer);
            assertNotNull("В ответе отсутствует или не заполнено название бургера (поле \"name\")!", response.path("name"));
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