package com.and.tim.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.model.Ingredient;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsActivity;
import com.and.tim.bakingapp.ui.step_list.StepListActivity;
import com.and.tim.bakingapp.viewmodel.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

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

//    @Override public void onRecipeListItemClick(int recipeId) {
    @Override public void onRecipeListItemClick(RecipeEntity recipe) {
//        viewModel.pinRecipe(recipe);
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(recipeKey, recipe.id);
        startActivity(intent);
    }

    @Override public void onPinRecipe(RecipeEntity recipe) {
        viewModel.pinRecipe(recipe);

//        Toast.makeText(this, recipe.name, Toast.LENGTH_SHORT).show();
    }
}
