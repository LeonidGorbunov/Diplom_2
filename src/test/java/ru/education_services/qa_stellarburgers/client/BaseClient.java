package ru.education_services.qa_stellarburgers.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseClient {

    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru"; //из тренажера https://stellarburgers.nomoreparties.site

    protected RequestSpecification getBaseSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}