package ru.education_services.qa_stellarburgers.tests;

import org.junit.Before;
import ru.education_services.qa_stellarburgers.client.OrderClient;
import ru.education_services.qa_stellarburgers.client.UserClient;

public abstract class BaseTest {

    protected UserClient userClient;
    protected OrderClient orderClient;

    @Before
    public void baseSetUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }
}