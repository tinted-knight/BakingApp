package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsActivity;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsFragment;
import com.and.tim.bakingapp.viewmodel.StepInstructionsViewModel;

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
        }
    }

    private void showStepList() {
        StepListFragment stepListFragment = StepListFragment.newInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragStepList, stepListFragment, StepListFragment.class.getCanonicalName())
                .commit();
    }

    private void runStepInstructionsActivity(int stepId) {
        Intent intent = new Intent(this, StepInstructionsActivity.class);
        intent.putExtra(recipeKey, recipeId);
        intent.putExtra(stepKey, stepId);
        startActivity(intent);
    }

    private void showStepInstructions(int stepId) {
        StepInstructionsViewModel viewModel = ViewModelProviders.of(this).get(StepInstructionsViewModel.class);
        viewModel.init(recipeId, stepId);
        StepInstructionsFragment fragment = StepInstructionsFragment.newInstance(recipeId, stepId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragStepInstructions, fragment, StepInstructionsFragment.class.getCanonicalName())
                .commit();
    }

    @Override public void onStepListItemClick(int stepId) {
        if (!modeTablet) {
            runStepInstructionsActivity(stepId);
        } else { // Mode tablet
            showStepInstructions(stepId);
        }
    }

    @Override public void onIngredItemClick() {
        Toast.makeText(this, "ingred click", Toast.LENGTH_SHORT).show();
    }
}
