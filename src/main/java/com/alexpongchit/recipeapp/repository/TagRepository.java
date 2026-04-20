package com.alexpongchit.recipeapp.repository;

import com.alexpongchit.recipeapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Tag persistence operations.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}