package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

public class StepListViewModel extends AndroidViewModel {

    public LiveData<StepListForRecipe> stepList;

    private RecipeListRepo repo;

    public StepListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(int recipeId) {
        repo = RecipeListRepo.get(getApplication());
        stepList = repo.getStepList(recipeId);
    }

}
