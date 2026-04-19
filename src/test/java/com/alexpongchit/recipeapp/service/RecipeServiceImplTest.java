package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.dto.ScaledRecipeResponse;
import com.alexpongchit.recipeapp.exception.ResourceNotFoundException;
import com.alexpongchit.recipeapp.model.Ingredient;
import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.model.Tag;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.repository.RecipeRepository;
import com.alexpongchit.recipeapp.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void createRecipe_shouldSaveAndReturnRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("Chicken and Rice");

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe savedRecipe = recipeService.createRecipe(recipe);

        assertNotNull(savedRecipe);
        assertEquals("Chicken and Rice", savedRecipe.getName());
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void getRecipeById_shouldReturnRecipeWhenFound() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Chicken and Rice");

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeService.getRecipeById(1L);

        assertTrue(result.isPresent());
        assertEquals("Chicken and Rice", result.get().getName());
    }

    @Test
    void getAllRecipes_shouldReturnRecipeList() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe 2");

        when(recipeRepository.findAll()).thenReturn(List.of(recipe1, recipe2));

        List<Recipe> recipes = recipeService.getAllRecipes();

        assertEquals(2, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void deleteRecipe_shouldCallRepositoryDeleteById() {
        recipeService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateRecipe_shouldUpdateRecipeFields() {
        User user = new User();
        user.setUsername("alex");

        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setName("Old Recipe");
        existingRecipe.setInstructions("Old instructions");
        existingRecipe.setUser(user);
        existingRecipe.setIngredients(new ArrayList<>());
        existingRecipe.setTags(new ArrayList<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Chicken Breast");
        ingredient.setQuantity(3.0);
        ingredient.setUnit("pieces");

        Tag tag = new Tag();
        tag.setName("high-protein");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("Updated Recipe");
        updatedRecipe.setInstructions("Updated instructions");
        updatedRecipe.setIngredients(new ArrayList<>(List.of(ingredient)));
        updatedRecipe.setTags(new ArrayList<>(List.of(tag)));

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Recipe result = recipeService.updateRecipe(1L, updatedRecipe);

        assertEquals("Updated Recipe", result.getName());
        assertEquals("Updated instructions", result.getInstructions());
        assertEquals(1, result.getIngredients().size());
        assertEquals("Chicken Breast", result.getIngredients().get(0).getName());
        assertEquals(1, result.getTags().size());
        assertEquals("high-protein", result.getTags().get(0).getName());
        assertEquals(result, result.getIngredients().get(0).getRecipe());
        assertEquals(result, result.getTags().get(0).getRecipe());

        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).save(existingRecipe);
    }

    @Test
    void updateRecipe_shouldThrowExceptionWhenRecipeNotFound() {
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("Updated Recipe");

        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recipeService.updateRecipe(999L, updatedRecipe));
    }

    @Test
    void scaleRecipe_shouldReturnScaledRecipeResponse() {
        User user = new User();
        user.setUsername("alex");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Chicken Breast");
        ingredient1.setQuantity(3.0);
        ingredient1.setUnit("pieces");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Rice");
        ingredient2.setQuantity(2.0);
        ingredient2.setUnit("cups");

        Tag tag = new Tag();
        tag.setName("meal prep");

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Chicken and Rice");
        recipe.setInstructions("Cook and combine.");
        recipe.setUser(user);
        recipe.setIngredients(List.of(ingredient1, ingredient2));
        recipe.setTags(List.of(tag));

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        ScaledRecipeResponse response = recipeService.scaleRecipe(1L, 2, 4);

        assertNotNull(response);
        assertEquals("Chicken and Rice", response.getName());
        assertEquals(2, response.getOriginalServings());
        assertEquals(4, response.getDesiredServings());
        assertEquals(2, response.getIngredients().size());
        assertEquals(6.0, response.getIngredients().get(0).getQuantity());
        assertEquals(4.0, response.getIngredients().get(1).getQuantity());
        assertEquals("meal prep", response.getTags().get(0));
    }

    @Test
    void scaleRecipe_shouldThrowExceptionWhenRecipeNotFound() {
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recipeService.scaleRecipe(999L, 2, 4));
    }
}