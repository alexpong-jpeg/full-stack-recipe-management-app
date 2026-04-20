package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.*;
import com.alexpongchit.recipeapp.exception.ResourceNotFoundException;
import com.alexpongchit.recipeapp.model.Ingredient;
import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.model.Tag;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.service.RecipeService;
import com.alexpongchit.recipeapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for recipe-related API operations.
 *
 * This controller handles recipe creation, retrieval, search, update, deletion,
 * and ingredient scaling for the full-stack recipe management application.
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /**
     * Creates a new recipe from the incoming request payload and associates it
     * with an existing user.
     */
    @PostMapping
    public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeRequest request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setInstructions(request.getInstructions());
        recipe.setUser(user);

        List<Ingredient> ingredientList = new ArrayList<>();
        if (request.getIngredients() != null) {
            // Convert ingredient DTOs into entity objects and attach them
            // to the parent recipe so the relationship persists correctly.
            for (IngredientRequest ingredientRequest : request.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientRequest.getName());
                ingredient.setQuantity(ingredientRequest.getQuantity());
                ingredient.setUnit(ingredientRequest.getUnit());
                ingredient.setRecipe(recipe);
                ingredientList.add(ingredient);
            }
        }
        recipe.setIngredients(ingredientList);

        List<Tag> tagList = new ArrayList<>();
        if (request.getTags() != null) {
            // Convert incoming tag strings into Tag entities linked to the recipe.
            for (String tagName : request.getTags()) {
                Tag tag = new Tag();
                tag.setName(tagName);
                tag.setRecipe(recipe);
                tagList.add(tag);
            }
        }
        recipe.setTags(tagList);

        Recipe savedRecipe = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedRecipe));
    }

    /**
     * Returns all recipes as response DTOs.
     */
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> responses = recipeService.getAllRecipes()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    /**
     * Returns a single recipe by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        return ResponseEntity.ok(mapToResponse(recipe));
    }

    /**
     * Searches for recipes whose names contain the supplied text.
     */
    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> searchRecipes(@RequestParam String name) {
        List<RecipeResponse> responses = recipeService.getRecipesByName(name)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    /**
     * Deletes a recipe by ID after confirming it exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("Recipe deleted successfully");
    }

    /**
     * Updates the core fields, ingredients, and tags for an existing recipe.
     * The existing owner is preserved rather than changed from the request body.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest request) {
        Recipe existingRecipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName(request.getName());
        updatedRecipe.setInstructions(request.getInstructions());
        updatedRecipe.setUser(existingRecipe.getUser());

        List<Ingredient> ingredientList = new ArrayList<>();
        if (request.getIngredients() != null) {
            for (IngredientRequest ingredientRequest : request.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientRequest.getName());
                ingredient.setQuantity(ingredientRequest.getQuantity());
                ingredient.setUnit(ingredientRequest.getUnit());
                ingredientList.add(ingredient);
            }
        }
        updatedRecipe.setIngredients(ingredientList);

        List<Tag> tagList = new ArrayList<>();
        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                Tag tag = new Tag();
                tag.setName(tagName);
                tagList.add(tag);
            }
        }
        updatedRecipe.setTags(tagList);

        Recipe savedRecipe = recipeService.updateRecipe(id, updatedRecipe);
        return ResponseEntity.ok(mapToResponse(savedRecipe));
    }

    /**
     * Returns a scaled version of the recipe's ingredients without modifying
     * the stored recipe in the database.
     */
    @PostMapping("/{id}/scale")
    public ResponseEntity<ScaledRecipeResponse> scaleRecipe(@PathVariable Long id, @Valid @RequestBody ScaleRecipeRequest request) {
        ScaledRecipeResponse response = recipeService.scaleRecipe(
                id,
                request.getOriginalServings(),
                request.getDesiredServings()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Maps a Recipe entity into a simpler API response object.
     */
    private RecipeResponse mapToResponse(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setInstructions(recipe.getInstructions());
        response.setUsername(recipe.getUser().getUsername());

        // Flatten ingredient objects into display-friendly strings for the frontend.
        response.setIngredients(
                recipe.getIngredients().stream()
                        .map(i -> i.getQuantity() + " " + i.getUnit() + " " + i.getName())
                        .toList()
        );

        response.setTags(
                recipe.getTags().stream()
                        .map(Tag::getName)
                        .toList()
        );
        return response;
    }
}