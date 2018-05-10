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

public class StepListActivity extends AppCompatActivity
        implements StepListAdapter.StepListItemClickListener, IngredientsAdapter.IngredItemClickListener {

    private int recipeId;
    private boolean modeTablet = false;

    public static final String ACTION_STEP_LIST = "show_step_list";
    public static final String ACTION_STEP_INSTRUCTIONS = "show_step_instructions";

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;
    @BindString(R.string.stepKey) String stepKey;

    private StepInstructionsVM stepInstructionsVM;
    private StepInstructionsFragment stepInstructionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        if (findViewById(R.id.layoutSw600) != null) modeTablet = true;

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                switch (intent.getAction()) {
                    case ACTION_STEP_LIST: {
                        recipeId = intent.getIntExtra(recipeKey, 0);
                        showStepList();
                        if (modeTablet) showStepInstructionsHere(0);
                        break;
                    }
                    case ACTION_STEP_INSTRUCTIONS: {
                        recipeId = intent.getIntExtra(recipeKey, 0);
                        int stepId = intent.getIntExtra(stepKey, 0);
                        showStepList();
                        if (modeTablet)
                            showStepInstructionsHere(stepId);
                        else
                            showStepInstructionsSeparate(stepId);
                        Toast.makeText(this, "yahooooooooo", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Unsupported intent action");
                }
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
//        StepListViewModel.MyFactory factory = new StepListViewModel.MyFactory(getApplication(), recipeId);
//        StepListViewModel stepListViewModel = ViewModelProviders.of(this, factory).get(StepListViewModel.class);
        StepListFragment stepListFragment = StepListFragment.newInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragStepList, stepListFragment)
                .commit();
    }

    private void showStepInstructionsHere(int stepId) {
        setupInstructionsViewModel(stepId);
        if (stepInstructionsFragment == null) {
            stepInstructionsFragment = StepInstructionsFragment.newInstance(recipeId, stepId);
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragStepInstructions, stepInstructionsFragment)
                    .commit();
        }
    }

    private void showStepInstructionsSeparate(int data) {
        setupInstructionsViewModel(data);
        StepInstructionsFragment fragment = StepInstructionsFragment.newInstance(recipeId, data);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("TAGG")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragStepList, fragment)
                .commit();
    }

    void setupInstructionsViewModel(int stepId) {
        if (stepInstructionsVM != null) stepInstructionsVM.setStep(stepId);
        StepInstructionsVM.MyFactory factory =
                new StepInstructionsVM.MyFactory(getApplication(), recipeId, stepId);
        stepInstructionsVM = ViewModelProviders.of(this, factory).get(StepInstructionsVM.class);
    }

    @Override public void onTest(int data) {
        if (modeTablet)
            showStepInstructionsHere(data);
        else {
            showStepInstructionsSeparate(data);
        }
    }

    @Override public void onIngredItemClick() {
        Toast.makeText(this, "ingred click", Toast.LENGTH_SHORT).show();
    }
}
