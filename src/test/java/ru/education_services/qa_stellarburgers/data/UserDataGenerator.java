package ru.education_services.qa_stellarburgers.data;

import ru.education_services.qa_stellarburgers.models.request.UserRegisterRequest;
import java.util.UUID;

public class UserDataGenerator {

    public static UserRegisterRequest getDefaultUser() {

        String uniqueSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        String uniqueEmail = "simpson" + uniqueSuffix + "@yandex.ru";
        String uniquePassword = "password" + uniqueSuffix;
        String uniqueName = "homer" + uniqueSuffix;
        return new UserRegisterRequest(uniqueEmail, uniquePassword, uniqueName);
    }
}
