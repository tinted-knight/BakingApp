package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.and.tim.bakingapp.base.Marker;
import com.and.tim.bakingapp.model.Ingredient;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipes")
public class RecipeEntity implements Marker {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    public int id;

    public String name;

    public Integer servings;

    public String image;

    public Boolean pinned;

    public Integer stepCount;

    public Integer ingredientsCount;

    @Ignore private List<StepEntity> steps;

    @Ignore private List<IngredientEntity> ingredients;

    public RecipeEntity(Recipe recipe, Boolean pinned) {
        this(recipe.getId(), recipe.getName(), recipe.getSteps(), recipe.getIngredients(), recipe.getServings(), recipe.getImage(), pinned);
    }

    private RecipeEntity(int id, String name, List<Step> steps, List<Ingredient> ingredients, Integer servings, String image, Boolean pinned) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.pinned = pinned;

        this.stepCount = steps.size() - 1;
        this.ingredientsCount = ingredients.size() - 1;

        this.steps = new ArrayList<>();
        for (Step s : steps) {
            StepEntity stepEntity = new StepEntity(s, id);
            this.steps.add(stepEntity);
        }

        this.ingredients = new ArrayList<>();
        for (Ingredient i : ingredients) {
            IngredientEntity ingredientEntity = new IngredientEntity(i, id);
            this.ingredients.add(ingredientEntity);
        }
    }

    public RecipeEntity() {
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

}
