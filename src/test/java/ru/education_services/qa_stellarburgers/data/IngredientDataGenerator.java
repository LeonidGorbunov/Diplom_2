package ru.education_services.qa_stellarburgers.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientDataGenerator {

    private static final int INGREDIENT_ID_LENGTH = 24;
    private static final int INGREDIENTS_COUNT = 2;

    public static List<String> getNonExistentIngredients(List<String> realIngredients) {

        List<String> nonExistentIngredients = new ArrayList<>();

        for (int i = 0; i < INGREDIENTS_COUNT; i++) {
            String uniqueHash = generateUniqueNonExistentHash(realIngredients, nonExistentIngredients);
            nonExistentIngredients.add(uniqueHash);
        }

        return nonExistentIngredients;
    }

    private static String generateUniqueNonExistentHash(List<String> realIngredients, List<String> currentGenerated) {

        String randomHash;

        do {
            randomHash = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, INGREDIENT_ID_LENGTH);
        } while (realIngredients.contains(randomHash) || currentGenerated.contains(randomHash));

        return randomHash;
    }
}
