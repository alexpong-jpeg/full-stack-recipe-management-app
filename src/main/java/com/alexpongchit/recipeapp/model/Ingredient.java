package com.alexpongchit.recipeapp.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single ingredient associated with a recipe.
 */
@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double quantity;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}