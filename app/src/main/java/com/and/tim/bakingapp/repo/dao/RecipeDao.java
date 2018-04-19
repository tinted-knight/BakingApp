package com.and.tim.bakingapp.repo.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("select * from recipes")
    LiveData<List<RecipeEntity>> getRecipeList();

    @Query("select * from recipes where pinned = 1")
    RecipeEntity getPinnedRecipe();

    @Query("select * from recipes where _id = :id ")
    LiveData<RecipeEntity> getById(int id);

    @Query("select _id, name from recipes where _id = :recipeId")
    LiveData<StepListForRecipe> getStepListForRecipe(int recipeId);

    @Query("select _id, name from recipes where _id = :recipeId")
    StepListForRecipe getStepListForWidget(int recipeId);

    @Query("select * from steps where steps._id = :stepId")
    LiveData<StepEntity> getStepById(int stepId);

    @Query("select * from steps where steps.recipeId = :recipeId order by steps._id asc limit 1")
    LiveData<StepEntity> getFirstStepForRecipe(int recipeId);

    @Query("select max(_id) from steps where steps.recipeId = :recipeId")
    Integer getMaxStepId(int recipeId);

    @Query("select min(_id) from steps where steps.recipeId = :recipeId")
    Integer getMinStepId(int recipeId);

    @Insert
    void insertRecipe(RecipeEntity... recipeEntity);

    @Insert
    void insertStep(StepEntity... stepEntities);

    @Insert
    void insertIngredient(IngredientEntity... ingredientEntities);

    @Update()
    void pinRecipe(RecipeEntity entity);
}
