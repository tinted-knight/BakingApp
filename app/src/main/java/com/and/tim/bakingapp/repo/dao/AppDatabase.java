package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.and.tim.bakingapp.repo.dao.entities.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.entities.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.entities.StepEntity;

@Database(entities = {RecipeEntity.class, StepEntity.class, IngredientEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract RecipeDao recipeDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,"BakingApp")
                    .allowMainThreadQueries()
                    .build();
            return instance;
        }
        return instance;
    }

}
