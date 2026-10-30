package ru.education_services.qa_stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.education_services.qa_stellarburgers.models.request.OrderCreateRequest;
import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Действие: создание заказа, post /api/orders")
    public Response actionOrderCreate (OrderCreateRequest orderRequest, String accessToken) {

        var requestSpecification = given(BASE_SPEC).body(orderRequest);

        if (accessToken != null) {
            requestSpecification.header("Authorization", accessToken);
        }

        return requestSpecification
                .when()
                .post("/api/orders");
    }

    @Step("Действие: получение списка ингредиентов, get /api/ingredients")
    public Response actionGetIngredients () {
        return given(BASE_SPEC)
                .when()
                .get("/api/ingredients");
    }
}
