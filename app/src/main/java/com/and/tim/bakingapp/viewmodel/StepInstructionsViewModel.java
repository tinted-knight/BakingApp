package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.StepEntity;

public class StepInstructionsViewModel extends AndroidViewModel {

    public static final int NO_STEP = -1;
    public LiveData<StepEntity> stepData;

    private RecipeListRepo repo;
    private int recipeId;
    private int stepId;
    private int maxStepId;
    private int minStepId;
    private MutableLiveData<Boolean> hasNextStep;
    private MutableLiveData<Boolean> hasPreviousStep;
//    private LiveData<RecipeEntity> recipeData;

    public StepInstructionsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(int recipeId, int stepId) {
        this.stepId = stepId;
        this.recipeId = recipeId;

        repo = RecipeListRepo.get(getApplication());
        if (stepId != NO_STEP)
            stepData = repo.getStepById(recipeId, stepId);
        else
            stepData = repo.getFirstStepForRecipe(recipeId);

//        RecipeEntity recipeData = repo.getRecipeById(recipeId).getValue();
        maxStepId = repo.getMaxStepId(recipeId);
        minStepId = repo.getMinStepId(recipeId);

        initHasNextStep();
        initHasPreviousStep();
    }

    public void stepForward() {
        stepId++;
        fetchSteps();
        stepData = repo.getStepById(recipeId, stepId);
    }

    public void stepBack() {
        stepId--;
        fetchSteps();
        stepData = repo.getStepById(recipeId, stepId);
    }

    public LiveData<Boolean> getHasNextStep() {
        return hasNextStep;
    }

    public LiveData<Boolean> getHasPreviousStep() {
        return hasPreviousStep;
    }

    public void setStepId(int id) {
        stepId = id;
    }

    private void fetchSteps() {
        // check for Next Step
        fetchStep(canMoveForward(), hasNextStep);
        // check for Previous Step
        fetchStep(canMoveBack(), hasPreviousStep);
    }

    private void fetchStep(boolean canMove, MutableLiveData<Boolean> value) {
        boolean enabled = value.getValue();
        if (canMove) {
            if (!enabled) value.setValue(true);
        } else { // !canMove
            if (enabled) value.setValue(false);
        }
    }

    private void initHasNextStep() {
        hasNextStep = new MutableLiveData<>();
        hasNextStep.setValue(false);
        if (canMoveForward()) hasNextStep.setValue(true);
    }

    private void initHasPreviousStep() {
        hasPreviousStep = new MutableLiveData<>();
        hasPreviousStep.setValue(false);
        if (canMoveBack()) hasPreviousStep.setValue(true);
    }

    private boolean canMoveForward() {
        return stepId < maxStepId;
    }

    private boolean canMoveBack() {
        return stepId - 1 >= minStepId;
    }

}
