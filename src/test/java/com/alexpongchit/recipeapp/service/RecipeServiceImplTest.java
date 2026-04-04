package com.alexpongchit.recipeapp.service;

import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.repository.RecipeRepository;
import com.alexpongchit.recipeapp.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}