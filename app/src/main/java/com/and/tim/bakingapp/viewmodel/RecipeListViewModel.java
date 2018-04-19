package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

//    public LiveData<List<Recipe>> data;
    public LiveData<List<RecipeEntity>> data;

    private RecipeListRepo repo;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        repo = RecipeListRepo.get(getApplication());
//        data = repo.getRecipeList();
        data = repo.getRecipeList();
    }

    public void pinRecipe(RecipeEntity recipe) {
        repo.pinRecipe(recipe);
    }

}
