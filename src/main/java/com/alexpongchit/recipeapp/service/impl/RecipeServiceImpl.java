package com.alexpongchit.recipeapp.service.impl;

import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.repository.RecipeRepository;
import com.alexpongchit.recipeapp.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the RecipeService interface.
 *
 * Phase I uses this service to manage foundational recipe operations such as
 * creation, retrieval, searching, updating, and deletion.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Saves a new recipe to the database.
     */
    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Returns all recipes currently stored in the database.
     */
    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    /**
     * Retrieves a recipe by its database identifier.
     */
    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    /**
     * Searches recipes by name using a case-insensitive partial match.
     */
    @Override
    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Updates the main editable fields of an existing recipe.
     */
    @Override
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    existingRecipe.setName(updatedRecipe.getName());
                    existingRecipe.setInstructions(updatedRecipe.getInstructions());
                    existingRecipe.setIngredients(updatedRecipe.getIngredients());
                    existingRecipe.setTags(updatedRecipe.getTags());
                    return recipeRepository.save(existingRecipe);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }

    /**
     * Deletes a recipe by its database identifier.
     */
    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}