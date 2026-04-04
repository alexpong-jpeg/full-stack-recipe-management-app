package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Recipe createRecipe(Recipe recipe);
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(Long id);
    List<Recipe> getRecipesByName(String name);
    Recipe updateRecipe(Long id, Recipe updatedRecipe);
    void deleteRecipe(Long id);
}