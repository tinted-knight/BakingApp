package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.ui.widget.IngredientsListService;
import com.and.tim.bakingapp.ui.widget_step_list.StepListService;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    public LiveData<List<RecipeEntity>> data;

    private RecipeListRepo repo;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        repo = RecipeListRepo.get(getApplication());
        data = repo.getRecipeList();
    }

    public void pinRecipe(RecipeEntity recipe) {
        repo.pinRecipe(recipe);
        IngredientsListService.startActionUpdateWidget(getApplication());
        StepListService.startActionUpdateWidget(getApplication());
    }

//    public int getStepCount(int recipeId) {
//        return repo.getStepCount(recipeId);
//    }

}
