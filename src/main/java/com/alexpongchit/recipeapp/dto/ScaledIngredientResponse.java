package com.alexpongchit.recipeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO representing a single scaled ingredient in a scaled recipe response.
 */
@Getter
@AllArgsConstructor
public class ScaledIngredientResponse {
    private String name;
    private Double quantity;
    private String unit;
}