package ru.education_services.qa_stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.education_services.qa_stellarburgers.client.UserClient;

public class ApiUserTeardownSteps {

    private final UserClient userClient = new UserClient();

    @Step("TEARDOWN: удаление пользователя на бэкенде по токену")
    public void deleteUserSafely(String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            return;
        }

        try {
            Response response = userClient.actionUserDelete(accessToken);

            if (response.getStatusCode() != HttpStatus.SC_ACCEPTED &&
                    response.getStatusCode() != HttpStatus.SC_OK) {
                System.err.println("[Warning] Пользователь не удален. Статус ответа бэкенда: " + response.getStatusCode());
            }

        } catch (JsonPathException e) {
            System.err.println("[Error] Ошибка парсинга ответа при удалении пользователя: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[Error] Сетевая ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}
