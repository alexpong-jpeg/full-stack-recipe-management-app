package com.alexpongchit.recipeapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientRequest {
    private String name;
    private Double quantity;
    private String unit;
}