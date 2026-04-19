package com.alexpongchit.recipeapp.controller;

import com.alexpongchit.recipeapp.dto.ScaledIngredientResponse;
import com.alexpongchit.recipeapp.dto.ScaledRecipeResponse;
import com.alexpongchit.recipeapp.exception.GlobalExceptionHandler;
import com.alexpongchit.recipeapp.model.Recipe;
import com.alexpongchit.recipeapp.model.User;
import com.alexpongchit.recipeapp.security.JwtAuthenticationFilter;
import com.alexpongchit.recipeapp.service.RecipeService;
import com.alexpongchit.recipeapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RecipeController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getRecipeById_shouldReturnRecipeWhenFound() throws Exception {
        User user = new User();
        user.setUsername("alex");

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Chicken and Rice");
        recipe.setInstructions("Cook and combine.");
        recipe.setUser(user);
        recipe.setIngredients(List.of());
        recipe.setTags(List.of());

        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Chicken and Rice"))
                .andExpect(jsonPath("$.username").value("alex"));
    }

    @Test
    void getRecipeById_shouldReturnNotFoundWhenMissing() throws Exception {
        when(recipeService.getRecipeById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recipes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecipe_shouldReturnSuccessWhenRecipeExists() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully"));
    }

    @Test
    void updateRecipe_shouldReturnUpdatedRecipe() throws Exception {
        User user = new User();
        user.setUsername("alex");

        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setUser(user);

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        updatedRecipe.setName("Updated Chicken and Rice");
        updatedRecipe.setInstructions("Updated instructions");
        updatedRecipe.setUser(user);
        updatedRecipe.setIngredients(List.of());
        updatedRecipe.setTags(List.of());

        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(existingRecipe));
        when(recipeService.updateRecipe(eq(1L), any(Recipe.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Chicken and Rice",
                                  "instructions": "Updated instructions",
                                  "userId": 1,
                                  "ingredients": [],
                                  "tags": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Chicken and Rice"))
                .andExpect(jsonPath("$.username").value("alex"));
    }

    @Test
    void scaleRecipe_shouldReturnScaledRecipeResponse() throws Exception {
        ScaledRecipeResponse response = new ScaledRecipeResponse();
        response.setId(1L);
        response.setName("Chicken and Rice");
        response.setInstructions("Cook and combine.");
        response.setUsername("alex");
        response.setOriginalServings(2);
        response.setDesiredServings(4);
        response.setIngredients(List.of(
                new ScaledIngredientResponse("Chicken Breast", 6.0, "pieces")
        ));
        response.setTags(List.of("meal prep"));

        when(recipeService.scaleRecipe(1L, 2, 4)).thenReturn(response);

        mockMvc.perform(post("/api/recipes/1/scale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalServings": 2,
                                  "desiredServings": 4
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.desiredServings").value(4))
                .andExpect(jsonPath("$.ingredients[0].quantity").value(6.0));
    }
}