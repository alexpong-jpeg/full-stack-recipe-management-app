package com.alexpongchit.recipeapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScaledRecipeResponse {
    private Long id;
    private String name;
    private String instructions;
    private String username;
    private Integer originalServings;
    private Integer desiredServings;
    private List<ScaledIngredientResponse> ingredients;
    private List<String> tags;
}