package com.and.tim.bakingapp.repo.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.and.tim.bakingapp.repo.dao.entities.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.entities.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.entities.StepEntity;
import com.and.tim.bakingapp.repo.dao.query_models.IngredientsForRecipe;
import com.and.tim.bakingapp.repo.dao.query_models.RecipeStepsAndIngredients;
import com.and.tim.bakingapp.repo.dao.query_models.StepListForRecipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("select * from recipes")
    LiveData<List<RecipeEntity>> getRecipeList();

//    @Query("select count(steps._id) from steps where steps.recipeId = :recipeId")
//    Integer getStepCount(int recipeId);

    @Query("select * from recipes where pinned = 1")
    RecipeEntity getPinnedRecipe();

    @Transaction
    @Query("select _id, name from recipes where _id = :recipeId")
    LiveData<StepListForRecipe> getStepListForRecipe(int recipeId);

    @Transaction
    @Query("select _id, name from recipes where _id = :recipeId")
    LiveData<RecipeStepsAndIngredients> getRecipeLists(int recipeId);

    @Transaction
    @Query("select _id, name from recipes where _id = :recipeId")
    StepListForRecipe getStepListForWidget(int recipeId);

    @Transaction
    @Query("select _id, name from recipes where _id = :recipeId")
    IngredientsForRecipe getIngredientsForWidget(int recipeId);

    @Insert
    void insertRecipe(RecipeEntity... recipeEntity);

    @Insert
    void insertStep(StepEntity... stepEntities);

    @Insert
    void insertIngredient(IngredientEntity... ingredientEntities);

    @Query("update recipes set pinned = 0 where pinned = 1")
    void unpinAll();

    @Update()
    void updateSingeRecipe(RecipeEntity entity);
}
