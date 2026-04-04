package com.alexpongchit.recipeapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeResponse {
    private Long id;
    private String name;
    private String instructions;
    private String username;
    private List<String> ingredients;
    private List<String> tags;
}