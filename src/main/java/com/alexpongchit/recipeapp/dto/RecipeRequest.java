package com.alexpongchit.recipeapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO used to receive recipe creation data from the client.
 */
@Getter
@Setter
public class RecipeRequest {

    @NotBlank(message = "Recipe name is required")
    private String name;

    private String instructions;

    private Long userId;

    private List<IngredientRequest> ingredients;
    private List<String> tags;
}