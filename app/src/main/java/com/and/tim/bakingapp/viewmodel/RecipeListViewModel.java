package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.ui.widget_ingred_list.IngredientsListService;
import com.and.tim.bakingapp.ui.widget_step_list.StepListService;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    public LiveData<List<RecipeEntity>> data;
    public MediatorLiveData<Pair<LoadingState, String>> isLoading = new MediatorLiveData<>();

    public enum LoadingState {LOADING, DONE, ERROR}

    private RecipeListRepo repo;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        repo = RecipeListRepo.get(getApplication());
        data = repo.getRecipeList();
        observeIsLoading();
    }

    private void observeIsLoading() {
        isLoading.setValue(new Pair<>(LoadingState.LOADING, ""));
        isLoading.addSource(data, new Observer<List<RecipeEntity>>() {
            @Override public void onChanged(@Nullable List<RecipeEntity> entities) {
                if (entities != null) {
                    isLoading.setValue(new Pair<>(LoadingState.DONE, ""));
                    isLoading.removeSource(data);
                }
            }
        });
        isLoading.addSource(repo.getLoadingStatus(), new Observer<Pair<Boolean, String>>() {
            @Override public void onChanged(@Nullable Pair<Boolean, String> status) {
                if (status != null && status.first != null && !status.first) {
                    isLoading.setValue(new Pair<>(LoadingState.ERROR, status.second));
                }
            }
        });
    }

    public void switchPinRecipe(RecipeEntity recipe) {
        if (!recipe.pinned)
            repo.pinRecipe(recipe);
        else
            repo.unpinRecipe(recipe);

        IngredientsListService.startActionUpdateWidget(getApplication());
        StepListService.startActionUpdateWidget(getApplication());
    }

}
