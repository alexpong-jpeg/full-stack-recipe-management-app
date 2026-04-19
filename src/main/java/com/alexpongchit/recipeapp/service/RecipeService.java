package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.dto.ScaledRecipeResponse;
import com.alexpongchit.recipeapp.model.Recipe;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for recipe-related business operations.
 */
public interface RecipeService {
    Recipe createRecipe(Recipe recipe);
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(Long id);
    List<Recipe> getRecipesByName(String name);
    Recipe updateRecipe(Long id, Recipe updatedRecipe);
    void deleteRecipe(Long id);
    ScaledRecipeResponse scaleRecipe(Long recipeId, Integer originalServings, Integer desiredServings);
}