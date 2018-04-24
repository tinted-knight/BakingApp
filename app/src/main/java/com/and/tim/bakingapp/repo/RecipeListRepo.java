package com.and.tim.bakingapp.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.AppDatabase;
import com.and.tim.bakingapp.repo.dao.IngredientEntity;
import com.and.tim.bakingapp.repo.dao.RecipeDao;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.repo.dao.StepEntity;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListRepo {

    private RecipeListApi api;

    private AppDatabase db;
    private RecipeDao dao;
//    private Context context;

    private static RecipeListRepo instance = null;

    private MutableLiveData<List<RecipeEntity>> data;

    private SharedPreferences sp;

    //TODO check
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
                    List<RecipeEntity> result = new ArrayList<>();
                    for (Recipe r : response.body()) {
                        RecipeEntity re = new RecipeEntity(r, false);
                        result.add(re);
                    }
                    data.setValue(result);
                    fillLocalDatabase();
                    sp.edit().putBoolean("first_start", false).apply();
                }

                @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.d("TAGG", "onFailure: " + t.toString());
                }
            });
            return data;
        } else {
            return dao.getRecipeList();
        }
    }

//    public Integer getStepCount(int recipeId) {
//        return dao.getStepCount(recipeId);
//    }

    public LiveData<StepListForRecipe> getStepList(int recipeId) {
        return dao.getStepListForRecipe(recipeId);
    }

    public LiveData<StepEntity> getStepById(int recipeId, int stepId) {
        return dao.getStepById(stepId);
    }

    public LiveData<StepEntity> getFirstStepForRecipe(int recipeId) {
        return dao.getFirstStepForRecipe(recipeId);
    }

    public int getMaxStepId(int recipeId) {
        return dao.getMaxStepId(recipeId);
    }

    public int getMinStepId(int recipeId) {
        return dao.getMinStepId(recipeId);
    }

    public void pinRecipe(RecipeEntity recipeEntity) {
//        RecipeEntity recipeEntity = dao.getById(recipeId).getValue();
        recipeEntity.pinned = true;
        dao.unpinAll();
        dao.pinRecipe(recipeEntity);
    }

    public RecipeEntity getPinnedRecipe() {
        return dao.getPinnedRecipe();
    }

    public StepListForRecipe getStepListForWidget(int recipeId) {
        return dao.getStepListForWidget(recipeId);
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
