package ru.education_services.qa_stellarburgers.models.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

    @SerializedName("ingredients")
    private List<String> ingredients;
}