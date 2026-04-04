package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.IngredientRequest;
import com.alexpongchit.recipeapp.dto.RecipeRequest;
import com.alexpongchit.recipeapp.dto.RecipeResponse;
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
 * Phase I focuses on foundational CRUD behavior, allowing recipes to be created,
 * retrieved, searched, and deleted through the backend API.
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
     * Creates a new recipe and maps incoming DTO data into entity objects.
     */
    @PostMapping
    public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeRequest request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setInstructions(request.getInstructions());
        recipe.setUser(user);

        List<Ingredient> ingredientList = new ArrayList<>();
        if (request.getIngredients() != null) {
            // Build Ingredient entities from the request payload and attach them to the recipe.
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
            // Build Tag entities from tag names so they can be persisted with the recipe.
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
     * Retrieves all recipes and converts them into response DTOs.
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
     * Retrieves a single recipe by its database identifier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .<ResponseEntity<?>>map(recipe -> ResponseEntity.ok(mapToResponse(recipe)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found"));
    }

    /**
     * Searches recipes by name using a case-insensitive partial match.
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
     * Deletes a recipe if it exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        if (recipeService.getRecipeById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
        }

        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("Recipe deleted successfully");
    }

    /**
     * Converts a Recipe entity into a RecipeResponse DTO for cleaner API responses.
     */
    private RecipeResponse mapToResponse(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setInstructions(recipe.getInstructions());
        response.setUsername(recipe.getUser().getUsername());
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