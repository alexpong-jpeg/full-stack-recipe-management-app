package com.alexpongchit.recipeapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScaleRecipeRequest {

    @NotNull(message = "Original servings value is required")
    @Min(value = 1, message = "Original servings must be at least 1")
    private Integer originalServings;

    @NotNull(message = "Desired servings value is required")
    @Min(value = 1, message = "Desired servings must be at least 1")
    private Integer desiredServings;
}