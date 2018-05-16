package com.and.tim.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.entities.RecipeEntity;
import com.and.tim.bakingapp.ui.step_list.StepListActivity;
import com.and.tim.bakingapp.viewmodel.RecipeListViewModel;

import butterknife.BindString;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements RecipeListAdapter.RecipeListItemClick {

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;

    private RecipeListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    }

    @Override public void onRecipeListItemClick(RecipeEntity recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.setAction(StepListActivity.ACTION_STEP_LIST);
        intent.putExtra(recipeKey, recipe.id);
        startActivity(intent);
    }

    @Override public void onPinRecipe(RecipeEntity recipe) {
        viewModel.switchPinRecipe(recipe);
    }
}
