package com.and.tim.bakingapp.ui.step_instrunctions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.viewmodel.StepInstructionsViewModel;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepInstructionsActivity extends AppCompatActivity {

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;
    @BindString(R.string.stepKey) String stepKey;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.btnPrevious) Button btnPrevious;

    private StepInstructionsViewModel viewModel;
    private int recipeId;
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_instructions);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            recipeId = intent.getIntExtra(recipeKey, 0);
            stepId = intent.getIntExtra(stepKey, 0);

            viewModel = getViewModel(recipeId, stepId);
            registerObservers();

            Fragment fragment = StepInstructionsFragment.newInstance(recipeId, stepId);
            naviReplaceFragment(fragment);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    viewModel.stepForward();
                    Fragment nextFragment = StepInstructionsFragment
                            .newInstance(recipeId, stepId);
                    naviReplaceFragment(nextFragment);
                }
            });

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    viewModel.stepBack();
                    Fragment prevFragment = StepInstructionsFragment
                            .newInstance(recipeId, stepId);
                    naviReplaceFragment(prevFragment);
                }
            });
        }
    }

    private StepInstructionsViewModel getViewModel(int recipeId, int stepId) {
        StepInstructionsViewModel viewModel = ViewModelProviders.of(this).get(StepInstructionsViewModel.class);
        viewModel.init(recipeId, stepId);
        return viewModel;
    }

    private void registerObservers() {
        viewModel.getHasNextStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean hasNextStep) {
                btnNext.setEnabled(hasNextStep);
            }
        });
        viewModel.getHasPreviousStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean hasPrevStep) {
                btnPrevious.setEnabled(hasPrevStep);
            }
        });
    }

    private void naviReplaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, fragment, StepInstructionsFragment.class.getCanonicalName())
                .commit();
    }

}
