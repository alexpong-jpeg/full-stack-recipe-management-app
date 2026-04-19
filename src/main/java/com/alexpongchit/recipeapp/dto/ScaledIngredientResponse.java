package com.alexpongchit.recipeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScaledIngredientResponse {
    private String name;
    private Double quantity;
    private String unit;
}