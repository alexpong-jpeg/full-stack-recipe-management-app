package com.alexpongchit.recipeapp.repository;

import com.alexpongchit.recipeapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Tag entities.
 *
 * Phase I uses this repository to support persistence of recipe tags and
 * categories. Custom query methods can be introduced in later phases as
 * search and filtering functionality becomes more advanced.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}