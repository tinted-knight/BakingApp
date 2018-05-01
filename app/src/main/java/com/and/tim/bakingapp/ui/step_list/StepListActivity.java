package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsActivity;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsFragment;
import com.and.tim.bakingapp.viewmodel.StepInstructionsVM;
import com.and.tim.bakingapp.viewmodel.StepListViewModel;

import butterknife.BindString;
import butterknife.ButterKnife;

import static com.and.tim.bakingapp.viewmodel.StepInstructionsViewModel.NO_STEP;

public class StepListActivity extends AppCompatActivity
        implements StepListAdapter.StepListItemClickListener, IngredientsAdapter.IngredItemClickListener {

    private int recipeId;
    private boolean modeTablet = false;

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;
    @BindString(R.string.stepKey) String stepKey;
    private StepInstructionsVM stepInstructionsVM;
    private StepListViewModel stepListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        if (findViewById(R.id.layoutSw600) != null) modeTablet = true;

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                recipeId = intent.getIntExtra(recipeKey, 0);
                showStepList();
                if (modeTablet) showStepInstructions(NO_STEP);
            }
        } else {
            recipeId = savedInstanceState.getInt(recipeKey);
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(recipeKey, recipeId);
    }

    private void showStepList() {
        StepListViewModel.MyFactory factory = new StepListViewModel.MyFactory(getApplication(), recipeId);
        stepListViewModel = ViewModelProviders.of(this, factory).get(StepListViewModel.class);
//        stepListViewModel.stepList.observe(this, new Observer<StepListForRecipe>() {
//            @Override public void onChanged(@Nullable StepListForRecipe list) {
//                getSupportActionBar().setTitle(list == null ? "..." : list.name);
//            }
//        });
        StepListFragment stepListFragment = StepListFragment.newInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragStepList, stepListFragment)
                .commit();
    }

    private void runStepInstructionsActivity(int stepId) {
        Intent intent = new Intent(this, StepInstructionsActivity.class);
        intent.putExtra(recipeKey, recipeId);
        intent.putExtra(stepKey, stepId);
        startActivity(intent);
    }

    private void showStepInstructions(int stepId) {
//        StepInstructionsViewModel viewModel = ViewModelProviders.of(this).get(StepInstructionsViewModel.class);
//        viewModel.init(recipeId, stepId);
        StepInstructionsFragment fragment = StepInstructionsFragment.newInstance(recipeId, stepId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragStepInstructions, fragment)
                .commit();
    }

    @Override public void onStepListItemClick(int stepId) {
        if (!modeTablet) {
//            runStepInstructionsActivity(stepId);
        } else { // Mode tablet
            showStepInstructions(stepId);
        }
    }

    void setupInstructionsViewModel(int stepId) {
        if (stepInstructionsVM != null) stepInstructionsVM.setStep(stepId);
        StepInstructionsVM.MyFactory factory =
                new StepInstructionsVM.MyFactory(getApplication(), recipeId, stepId);
        stepInstructionsVM = ViewModelProviders.of(this, factory).get(StepInstructionsVM.class);
    }

    @Override public void onTest(int data) {
        setupInstructionsViewModel(data);
        StepInstructionsFragment fragment = StepInstructionsFragment.newInstance(recipeId, data);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("TAGG")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragStepList, fragment)
                .commit();
    }

    @Override public void onIngredItemClick() {
        Toast.makeText(this, "ingred click", Toast.LENGTH_SHORT).show();
    }
}
