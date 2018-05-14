package com.and.tim.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.and.tim.bakingapp.repo.RecipeListRepo;
import com.and.tim.bakingapp.repo.dao.StepEntity;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

public class StepInstructionsVM extends AndroidViewModel {

    private MediatorLiveData<StepEntity> stepData = new MediatorLiveData<>();
    private LiveData<StepListForRecipe> stepList;
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>();
    private MutableLiveData<Integer> stepCount = new MutableLiveData<>();
    private MediatorLiveData<Boolean> hasNextStep = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> hasPreviousStep = new MediatorLiveData<>();
    private MediatorLiveData<Pair<Integer, Integer>> stepCounter = new MediatorLiveData<>();
    private MutableLiveData<String> recipeName = new MutableLiveData<>();

    private RecipeListRepo repo;
    private int recipeId;
    private int stepId;
    private int sCount;

//    public void setup(int recipeId, int stepId) {
//        this.recipeId = recipeId;
//        this.stepId = stepId;
//        this.sCount = -1;
//        repo = RecipeListRepo.get(getApplication());
//
//        stepList = repo.getStepList(recipeId);
//        currentStep.setValue(stepId);
//        initStepData();
//        observeHasNextStep();
//        observeHasPreviousStep();
//        initStepCounter();
//    }

    StepInstructionsVM(@NonNull Application application, int recipeId, int stepId) {
        super(application);

        this.recipeId = recipeId;
        this.stepId = stepId;
        this.sCount = -1;
        repo = RecipeListRepo.get(application);

        stepList = repo.getStepList(recipeId);
        currentStep.setValue(stepId);
        initStepData();
        observeHasNextStep();
        observeHasPreviousStep();
        initStepCounter();
    }

    private void initStepCounter() {
        stepCounter.addSource(stepCount, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer i) {
                if (i != null) {
                    if (currentStep.getValue() != null)
                        stepCounter.setValue(new Pair<>(currentStep.getValue() + 1, i + 1));
                    else stepCounter.setValue(new Pair<>(-1, i + 1));
                }
            }
        });
        stepCounter.addSource(currentStep, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer i) {
                if (i != null) {
                    if (stepCount.getValue() != null)
                        stepCounter.setValue(new Pair<>(i + 1, stepCount.getValue() + 1));
                    else stepCounter.setValue(new Pair<>(i + 1, -1));
                }
            }
        });
    }

    private void observeHasPreviousStep() {
        hasPreviousStep.addSource(currentStep, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer curStep) {
                hasPreviousStep.setValue(curStep != null && curStep > 0);
            }
        });
    }

    private void observeHasNextStep() {
        hasNextStep.addSource(currentStep, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer curStep) {
                Boolean result = false;
                if (curStep != null && stepCount.getValue() != null)
                    result = curStep < stepCount.getValue();
                hasNextStep.setValue(result);
            }
        });
        hasNextStep.addSource(stepCount, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer stepCount) {
                Boolean result = false;
                if (stepCount != null && currentStep.getValue() != null)
                    result = currentStep.getValue() < stepCount;
                hasNextStep.setValue(result);
            }
        });
    }

    private void initStepData() {
        // Init once
        stepData.addSource(stepList, new Observer<StepListForRecipe>() {
            @Override public void onChanged(@Nullable StepListForRecipe list) {
                if (list != null) {
                    stepData.setValue(list.steps.get(stepId));
                    sCount = list.steps.size() - 1;
                    stepCount.setValue(sCount);
                    recipeName.setValue(list.name);
                }
                stepData.removeSource(stepList);
            }
        });
        // Observe currentStep
        stepData.addSource(currentStep, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer value) {
                if (value != null) {
                    stepId = value;
                    if (stepList.getValue() != null)
                        stepData.setValue(stepList.getValue().steps.get(value));
                }
            }
        });
    }

    public LiveData<Boolean> getHasNextStep() {
        return hasNextStep;
    }

    public LiveData<Boolean> getHasPreviousStep() {
        return hasPreviousStep;
    }

    public LiveData<Pair<Integer, Integer>> getStepCounter() {
        return stepCounter;
    }

    public LiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    public LiveData<Integer> getStepCount() {
        return stepCount;
    }

    public LiveData<String> getRecipeName() {
        return recipeName;
    }

    public void doNextStep() {
        if (stepId < sCount)
            currentStep.setValue(++stepId);
    }

    public void doPreviousStep() {
        if (stepId > 0)
            currentStep.setValue(--stepId);
    }

    public void setStep(int stepId) {
        currentStep.setValue(stepId);
    }

    public LiveData<StepEntity> getStepData() {
        return stepData;
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
            return (T) new StepInstructionsVM(application, recipeId, stepId);
        }
    }

}
