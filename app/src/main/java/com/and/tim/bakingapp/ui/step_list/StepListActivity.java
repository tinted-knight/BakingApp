package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.test.EspressoIdlingResources;
import com.and.tim.bakingapp.ui.step_instrunctions.StepInstructionsFragment;
import com.and.tim.bakingapp.viewmodel.StepInstructionsVM;

import butterknife.BindString;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class StepListActivity extends AppCompatActivity
        implements StepListAdapter.StepListItemClickListener, IngredientsAdapter.IngredItemClickListener {

    @State int recipeId;
    private boolean modeTablet;
    private boolean modeTabletPortrait;

    public static final String ACTION_STEP_LIST = "show_step_list";
    public static final String ACTION_STEP_INSTRUCTIONS = "show_step_instructions";

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;
    @BindString(R.string.stepKey) String stepKey;

    private StepInstructionsVM stepInstructionsVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        modeTablet = findViewById(R.id.layoutSw600) != null;
        modeTabletPortrait = findViewById(R.id.layoutSw600Port) != null;

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.getAction() != null) {
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
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Unsupported intent action");
                }
            } else throw new UnsupportedOperationException("Intent action is null");
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState);
            if (modeTabletPortrait)
                placeFragmentsOnTabletLandToPortait();
            if (modeTablet)
                placeFragmentsOnTabletPortaitToLand();
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void showStepList() {
        StepListFragment stepListFragment = StepListFragment.newInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.fragStepList, stepListFragment, StepListFragment.class.getSimpleName())
                .commit();
    }

    private void showStepInstructionsHere(int stepId) {
        setupInstructionsViewModel(stepId);
        Fragment stepInstructionsFragment =
                getSupportFragmentManager()
                        .findFragmentByTag(StepInstructionsFragment.class.getSimpleName());
        if (stepInstructionsFragment == null) {
            stepInstructionsFragment = StepInstructionsFragment.newInstance(recipeId, stepId);
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragStepInstructions,
                            stepInstructionsFragment,
                            StepInstructionsFragment.class.getSimpleName())
                    .commit();
        }
    }

    private void showStepInstructionsSeparate(int data) {
        setupInstructionsViewModel(data);
        StepInstructionsFragment fragment = StepInstructionsFragment.newInstance(recipeId, data);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("BackStackTag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragStepList, fragment,
                        StepInstructionsFragment.class.getSimpleName())
                .commit();
    }

    private void setupInstructionsViewModel(int stepId) {
        if (stepInstructionsVM != null) {
            EspressoIdlingResources.increment();
            stepInstructionsVM.setStep(stepId);
        }
        StepInstructionsVM.MyFactory factory =
                new StepInstructionsVM.MyFactory(getApplication(), recipeId, stepId);
        stepInstructionsVM = ViewModelProviders.of(this, factory).get(StepInstructionsVM.class);
    }

    private void placeFragmentsOnTabletLandToPortait() {
        // Thanks to https://stackoverflow.com/a/17775067
        FragmentManager fm = getSupportFragmentManager();
        Fragment stepInstrFragment =
                fm.findFragmentByTag(StepInstructionsFragment.class.getSimpleName());
        if (stepInstrFragment == null) return;
        fm.beginTransaction().remove(stepInstrFragment).commit();
        fm.executePendingTransactions();

        fm.beginTransaction()
                .addToBackStack("BackStackTag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragStepList, stepInstrFragment,
                        StepInstructionsFragment.class.getSimpleName())
                .commit();
    }

    private void placeFragmentsOnTabletPortaitToLand() {
        // Thanks to https://stackoverflow.com/a/21328919
        FragmentManager fm = getSupportFragmentManager();
        Fragment stepListFragment =
                fm.findFragmentByTag(StepListFragment.class.getSimpleName());
        Fragment stepInstrFragment =
                fm.findFragmentByTag(StepInstructionsFragment.class.getSimpleName());

        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(stepListFragment);
        if (stepInstrFragment != null) transaction.remove(stepInstrFragment);
        transaction.commit();
        fm.executePendingTransactions();

        transaction = fm.beginTransaction();
        transaction.replace(R.id.fragStepList, stepListFragment, StepListFragment.class.getSimpleName());
        if (stepInstrFragment != null)
            transaction.replace(R.id.fragStepInstructions, stepInstrFragment,
                    StepInstructionsFragment.class.getSimpleName());
        transaction.commit();
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
