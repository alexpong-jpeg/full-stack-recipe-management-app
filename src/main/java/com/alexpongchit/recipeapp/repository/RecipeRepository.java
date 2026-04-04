package com.alexpongchit.recipeapp.repository;

import com.alexpongchit.recipeapp.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for recipe persistence and simple recipe search operations.
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByNameContainingIgnoreCase(String name);
}