package com.alexpongchit.recipeapp.service.impl;

import com.alexpongchit.recipeapp.dto.ScaledIngredientResponse;
import com.alexpongchit.recipeapp.dto.ScaledRecipeResponse;
import com.alexpongchit.recipeapp.exception.ResourceNotFoundException;
import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.repository.RecipeRepository;
import com.alexpongchit.recipeapp.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the RecipeService interface.
 * <p>
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

                    // Rebuild ingredient list and reassign recipe reference
                    existingRecipe.getIngredients().clear();
                    if (updatedRecipe.getIngredients() != null) {
                        updatedRecipe.getIngredients().forEach(ingredient -> {
                            ingredient.setRecipe(existingRecipe);
                            existingRecipe.getIngredients().add(ingredient);
                        });
                    }

                    // Rebuild tag list and reassign recipe reference
                    existingRecipe.getTags().clear();
                    if (updatedRecipe.getTags() != null) {
                        updatedRecipe.getTags().forEach(tag -> {
                            tag.setRecipe(existingRecipe);
                            existingRecipe.getTags().add(tag);
                        });
                    }

                    return recipeRepository.save(existingRecipe);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
    }

    /**
     * Deletes a recipe by its database identifier.
     */
    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public ScaledRecipeResponse scaleRecipe(Long recipeId, Integer originalServings, Integer desiredServings) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));

        double scaleFactor = (double) desiredServings / originalServings;

        ScaledRecipeResponse response = new ScaledRecipeResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setInstructions(recipe.getInstructions());
        response.setUsername(recipe.getUser().getUsername());
        response.setOriginalServings(originalServings);
        response.setDesiredServings(desiredServings);
        response.setIngredients(
                recipe.getIngredients().stream()
                        .map(ingredient -> new ScaledIngredientResponse(
                                ingredient.getName(),
                                ingredient.getQuantity() != null ? ingredient.getQuantity() * scaleFactor : null,
                                ingredient.getUnit()
                        ))
                        .toList()
        );
        response.setTags(
                recipe.getTags().stream()
                        .map(tag -> tag.getName())
                        .toList()
        );

        return response;
    }
}