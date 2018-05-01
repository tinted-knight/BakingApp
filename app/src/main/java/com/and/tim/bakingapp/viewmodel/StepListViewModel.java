package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

public class StepListViewModel extends AndroidViewModel {

    public LiveData<StepListForRecipe> stepList;

    private RecipeListRepo repo;

    StepListViewModel(@NonNull Application application, int recipeId) {
        super(application);
        repo = RecipeListRepo.get(getApplication());
        stepList = repo.getStepList(recipeId);
    }

//    public void init(int recipeId) {
//        repo = RecipeListRepo.get(getApplication());
//        stepList = repo.getStepList(recipeId);
//    }

    public static class MyFactory extends ViewModelProvider.NewInstanceFactory {

        private final Application application;
        private final int recipeId;

        public MyFactory(Application application, int recipeId) {
            this.application = application;
            this.recipeId = recipeId;
        }

        @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StepListViewModel(application, recipeId);
        }
    }

}
