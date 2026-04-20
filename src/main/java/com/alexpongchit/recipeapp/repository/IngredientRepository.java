package com.alexpongchit.recipeapp.repository;

import com.alexpongchit.recipeapp.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Ingredient persistence operations.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}