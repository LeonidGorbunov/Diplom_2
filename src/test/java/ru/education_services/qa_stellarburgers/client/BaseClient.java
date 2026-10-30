package ru.education_services.qa_stellarburgers.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseClient {

    // это зеркало гораздо стабильнее стенда из ЯП: https://stellarburgers.education-services.ru
    private static final String BASE_URL = System.getProperty("site.url", "https://qa-stellarburgers.education-services.ru");

    protected static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .log(LogDetail.URI)
            .log(LogDetail.BODY)
            .build();
}