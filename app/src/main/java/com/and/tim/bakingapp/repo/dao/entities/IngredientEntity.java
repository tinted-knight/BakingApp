package com.and.tim.bakingapp.repo.dao.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.and.tim.bakingapp.json_model.Ingredient;

@Entity(tableName = "ingredients")
public class IngredientEntity {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public Float quantity;

    public String measure;

    public String ingredient;

    public int recipeId;

    public IngredientEntity() {}

    public IngredientEntity(Ingredient i, int recipeId) {
        quantity = i.getQuantity();
        measure = i.getMeasure();
        ingredient = i.getIngredient();
        this.recipeId = recipeId;
    }

}
