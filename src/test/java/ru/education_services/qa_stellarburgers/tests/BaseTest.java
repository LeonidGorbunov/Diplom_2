package ru.education_services.qa_stellarburgers.tests;

import org.junit.Before;
import ru.education_services.qa_stellarburgers.client.OrderClient;
import ru.education_services.qa_stellarburgers.client.UserClient;
import ru.education_services.qa_stellarburgers.steps.ApiUserTeardownSteps;

public abstract class BaseTest {

    protected UserClient userClient;
    protected OrderClient orderClient;
    protected ApiUserTeardownSteps apiUserTeardownSteps;

    @Before
    public void baseSetUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        apiUserTeardownSteps = new ApiUserTeardownSteps();
    }
}