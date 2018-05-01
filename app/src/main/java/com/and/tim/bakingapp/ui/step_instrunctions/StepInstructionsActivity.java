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
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.viewmodel.StepInstructionsVM;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepInstructionsActivity extends AppCompatActivity {

    //Bind
    @BindString(R.string.recipeKey) String recipeKey;
    @BindString(R.string.stepKey) String stepKey;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.btnPrevious) Button btnPrevious;
    @BindView(R.id.tvStepCount) TextView tvStepCount;
    @BindView(R.id.tvStepId) TextView tvStepId;

//    private StepInstructionsViewModel viewModel;
    private StepInstructionsVM viewModel;
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
//                    viewModel.stepForward();
                    viewModel.doNextStep();
                    Fragment nextFragment = new StepInstructionsFragment();
//                    Fragment nextFragment = StepInstructionsFragment
//                            .newInstance(recipeId, stepId);
                    naviReplaceFragment(nextFragment);
                }
            });

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
//                    viewModel.stepBack();
                    viewModel.doPreviousStep();
                    Fragment prevFragment = new StepInstructionsFragment();
//                    Fragment prevFragment = StepInstructionsFragment
//                            .newInstance(recipeId, stepId);
                    naviReplaceFragment(prevFragment);
                }
            });
        }
    }

//    private StepInstructionsViewModel getViewModel(int recipeId, int stepId) {
    private StepInstructionsVM getViewModel(int recipeId, int stepId) {
//        StepInstructionsViewModel.MyFactory factory =
//                new StepInstructionsViewModel.MyFactory(getApplication(), recipeId, stepId);
        StepInstructionsVM.MyFactory factory =
                new StepInstructionsVM.MyFactory(getApplication(), recipeId, stepId);
        return ViewModelProviders.of(this, factory).get(StepInstructionsVM.class);
    }

    private void registerObservers() {
        viewModel.getHasNextStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean value) {
                btnNext.setEnabled(value);
            }
        });
        viewModel.getHasPreviousStep().observe(this, new Observer<Boolean>() {
            @Override public void onChanged(@Nullable Boolean value) {
                btnPrevious.setEnabled(value);
            }
        });
        viewModel.getCurrentStep().observe(this, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer i) {
                String text = "Step " + String.valueOf(i + 1) + " of ";
                tvStepId.setText(text);
            }
        });
        viewModel.getStepCount().observe(this, new Observer<Integer>() {
            @Override public void onChanged(@Nullable Integer i) {
                tvStepCount.setText(String.valueOf(i + 1));
            }
        });
    }

    private void naviReplaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, fragment, StepInstructionsFragment.class.getCanonicalName())
                .commit();
    }

}
