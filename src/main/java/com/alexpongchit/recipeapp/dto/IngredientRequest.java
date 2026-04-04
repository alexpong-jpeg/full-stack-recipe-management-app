package com.alexpongchit.recipeapp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO used to receive ingredient data when creating a recipe.
 */
@Getter
@Setter
public class IngredientRequest {
    private String name;
    private Double quantity;
    private String unit;
}