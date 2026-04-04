package com.alexpongchit.recipeapp.repository;

import com.alexpongchit.recipeapp.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Ingredient entities.
 *
 * Phase I uses this repository to establish persistence support for recipe
 * ingredients as part of the backend foundation. Additional custom queries
 * may be added in later phases if ingredient-specific filtering or scaling
 * features require them.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}