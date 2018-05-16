package com.and.tim.bakingapp.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;

import com.and.tim.bakingapp.json_model.Recipe;
import com.and.tim.bakingapp.repo.dao.AppDatabase;
import com.and.tim.bakingapp.repo.dao.entities.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.query_models.IngredientsForRecipe;
import com.and.tim.bakingapp.repo.dao.RecipeDao;
import com.and.tim.bakingapp.repo.dao.entities.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.entities.StepEntity;
import com.and.tim.bakingapp.repo.dao.query_models.RecipeStepsAndIngredients;
import com.and.tim.bakingapp.repo.dao.query_models.StepListForRecipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListRepo {

    private RecipeListApi api;

    private AppDatabase db;
    private RecipeDao dao;

    private static RecipeListRepo instance = null;

    private MutableLiveData<List<RecipeEntity>> data;
    private MutableLiveData<Pair<Boolean, String>> loadingStatus;

    private SharedPreferences sp;

    public static RecipeListRepo get(Context context) {
        if (instance == null) {
            synchronized (RecipeListRepo.class) {
                if (instance == null) {
                    instance = new RecipeListRepo(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private RecipeListRepo(Context context) {
        api = RecipeListController.getApi();
        data = new MutableLiveData<>();
        loadingStatus = new MutableLiveData<>();
        db = AppDatabase.getInstance(context);
        dao = db.recipeDao();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public LiveData<List<RecipeEntity>> getRecipeList() {
        boolean firstStart = sp.getBoolean("first_start", true);
        if (firstStart) {
            api.getRecipeList().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    final List<RecipeEntity> result = new ArrayList<>();
                    for (Recipe r : response.body()) {
                        RecipeEntity re = new RecipeEntity(r, false);
                        result.add(re);
                    }
                    data.setValue(result);
                    loadingStatus.setValue(new Pair<>(true, ""));
                    fillLocalDatabase();
                    sp.edit().putBoolean("first_start", false).apply();
                }

                @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    loadingStatus.setValue(new Pair<>(false, t.getMessage()));
                }
            });
            return data;
        } else {
            return dao.getRecipeList();
        }
    }

    public LiveData<Pair<Boolean, String>> getLoadingStatus() {
        return loadingStatus;
    }

    public LiveData<StepListForRecipe> getStepList(int recipeId) {
        return dao.getStepListForRecipe(recipeId);
    }

    public LiveData<RecipeStepsAndIngredients> getRecipeLists(int recipeId) {
        return dao.getRecipeLists(recipeId);
    }

    public void pinRecipe(RecipeEntity recipeEntity) {
        recipeEntity.pinned = true;
        dao.unpinAll();
        dao.updateSingeRecipe(recipeEntity);
    }

    public void unpinRecipe(RecipeEntity recipeEntity) {
        recipeEntity.pinned = false;
        dao.updateSingeRecipe(recipeEntity);
    }

    public RecipeEntity getPinnedRecipe() {
        return dao.getPinnedRecipe();
    }

    public StepListForRecipe getStepListForWidget(int recipeId) {
        return dao.getStepListForWidget(recipeId);
    }

    public IngredientsForRecipe getIngredientsForWidget(int recipeId) {
        return dao.getIngredientsForWidget(recipeId);
    }

    private void fillLocalDatabase() {
        RecipeDao recipeDao = db.recipeDao();
        List<RecipeEntity> recipeList = data.getValue();
        recipeDao.insertRecipe(recipeList.toArray(new RecipeEntity[]{}));

        for (RecipeEntity re : recipeList) {
            recipeDao.insertStep(re.getSteps().toArray(new StepEntity[]{}));
            recipeDao.insertIngredient(re.getIngredients().toArray(new IngredientEntity[]{}));
        }
    }

}
