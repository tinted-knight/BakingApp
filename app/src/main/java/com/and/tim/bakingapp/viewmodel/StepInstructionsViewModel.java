package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.StepEntity;

public class StepInstructionsViewModel extends AndroidViewModel {

    public static final int NO_STEP = -1;
    private LiveData<StepEntity> stepData;

    private RecipeListRepo repo;
    private int recipeId;
    private int stepId;
    private int stepCount = -1;
    private LiveData<Integer> maxStepId;
    private LiveData<Integer> minStepId;
    private MediatorLiveData<Boolean> hasNextStep;
    private MediatorLiveData<Boolean> hasPrevStep;
    private MediatorLiveData<Pair<Integer, Integer>> stepCountPair;

    public StepInstructionsViewModel(@NonNull Application application, int recipeId, int stepId) {
        super(application);
        init(recipeId, stepId);
    }

    public void init(int recipeId, int stepId) {
        this.stepId = stepId;
        this.recipeId = recipeId;

        repo = RecipeListRepo.get(getApplication());

        maxStepId = repo.getMaxStepId(recipeId);
        minStepId = repo.getMinStepId(recipeId);

        if (stepId != NO_STEP) {
            stepData = repo.getStepById(recipeId, stepId);
        } else {
            stepData = repo.getFirstStepForRecipe(recipeId);
        }

        initHasNextStep();
        initHasPrevStep();
        initStepCounter();
    }

    private void initStepCounter() {
        stepCountPair = new MediatorLiveData<>();
        stepCountPair.addSource(maxStepId, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer value) {
                if (value != null) {
                    stepCountPair.setValue(new Pair<>(stepId, value));
                    stepCount = value;
                }
            }
        });
        stepCountPair.addSource(stepData, new Observer<StepEntity>() {
            @Override public void onChanged(@Nullable StepEntity stepEntity) {
                if (stepEntity != null && stepCount != -1) {
                    stepCountPair.setValue(new Pair<>(stepEntity.stepId, stepCount));
                }
            }
        });
    }

    public void stepForward() {
        stepId++;
        stepData = repo.getStepById(recipeId, stepId);
        swapMediatorData();
    }

    public void stepBack() {
        stepId--;
        stepData = repo.getStepById(recipeId, stepId);
        swapMediatorData();
    }

    private Observer<StepEntity> nextStepObserver = new Observer<StepEntity>() {
        @Override public void onChanged(@Nullable StepEntity stepEntity) {
            if (stepEntity == null) hasNextStep.setValue(false);
            else {
                if (maxStepId.getValue() == null) hasNextStep.setValue(false);
                else hasNextStep.setValue(stepEntity._id < maxStepId.getValue());
            }
        }
    };

    private Observer<StepEntity> prevStepObserver = new Observer<StepEntity>() {
        @Override public void onChanged(@Nullable StepEntity stepEntity) {
            if (stepEntity == null) hasPrevStep.setValue(false);
            else {
                if (minStepId.getValue() == null) hasPrevStep.setValue(false);
                else hasPrevStep.setValue(stepEntity._id > minStepId.getValue());
            }
        }
    };

    private void initHasNextStep() {
        hasNextStep = new MediatorLiveData<>();
        hasNextStep.addSource(stepData, nextStepObserver);
        hasNextStep.addSource(maxStepId, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer value) {
                if (value == null) hasNextStep.setValue(false);
                else hasNextStep.setValue(stepId < value);
            }
        });
    }

    private void initHasPrevStep() {
        hasPrevStep = new MediatorLiveData<>();
        hasPrevStep.addSource(stepData, prevStepObserver);
        hasPrevStep.addSource(minStepId, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer value) {
                if (value == null) hasPrevStep.setValue(false);
                else hasPrevStep.setValue(stepId > value);
            }
        });
    }

    private void swapMediatorData() {
        hasNextStep.removeSource(stepData);
        hasNextStep.addSource(stepData, nextStepObserver);
        hasPrevStep.removeSource(stepData);
        hasPrevStep.addSource(stepData, prevStepObserver);
    }

    public LiveData<Boolean> getHasNextStep() {
        return hasNextStep;
    }

    public LiveData<Boolean> getHasPrevStep() {
        return hasPrevStep;
    }

    public LiveData<Pair<Integer, Integer>> getStepCountPair() {
        return stepCountPair;
    }

    public void setStepId(int id) {
        stepId = id;
    }

    public static class MyFactory extends ViewModelProvider.NewInstanceFactory {

        private final Application application;
        private final int stepId;
        private final int recipeId;

        public MyFactory(Application application, int recipeId, int stepId) {
            this.application = application;
            this.stepId = stepId;
            this.recipeId = recipeId;
        }

        @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StepInstructionsViewModel(application, recipeId, stepId);
        }
    }

}
