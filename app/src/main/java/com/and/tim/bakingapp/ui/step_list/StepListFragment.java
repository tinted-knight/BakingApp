package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;
import com.and.tim.bakingapp.viewmodel.StepListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class StepListFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    private int recipeId;
    @State boolean ingredListExpanded = false;
    @State int scrollX = 0;
    @State int scrollY = 0;

    private StepListAdapter stepAdapter;
    private IngredientsAdapter ingredAdapter;

    private StepListViewModel viewModel;

    //Binds
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.rvStepList) RecyclerView rvStepList;
    @BindView(R.id.rvIngredList) RecyclerView rvIngredList;
    @BindView(R.id.btnExpandCollapse) ImageButton btnExpandCollapse;
    @BindView(R.id.layoutSteps) LinearLayout layoutSteps;
    @BindView(R.id.nestedScrollView) NestedScrollView scrollView;


    public StepListFragment() {
        // Required empty public constructor
    }

    public static StepListFragment newInstance(int recipeId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, root);

        setupStepList();
        setupIngredientList();

        return root;
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
        }
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
    }

    private void setupIngredientList() {
        rvIngredList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvIngredList.setNestedScrollingEnabled(false);

        if (ingredAdapter == null)
            ingredAdapter = new IngredientsAdapter(
                    (IngredientsAdapter.IngredItemClickListener) getActivity(),
                    R.layout.item_ingredient);
        rvIngredList.setAdapter(ingredAdapter);

        if (ingredListExpanded) expandIngedientList();
        else collapseIngedList();

        btnExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (ingredListExpanded) collapseIngedList();
                else expandIngedientList();
            }
        });
    }

    private void expandIngedientList() {
        TransitionManager.beginDelayedTransition(layoutSteps);
        rvIngredList.setVisibility(View.VISIBLE);
        ingredListExpanded = true;
        btnExpandCollapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
    }

    private void collapseIngedList() {
        TransitionManager.beginDelayedTransition(layoutSteps);
        rvIngredList.setVisibility(View.GONE);
        ingredListExpanded = false;
        btnExpandCollapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
    }

    private void setupStepList() {
        rvStepList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvStepList.addItemDecoration(new DividerItemDecoration(
                getActivity(),
                DividerItemDecoration.VERTICAL
        ));
        rvStepList.setNestedScrollingEnabled(false);
        if (stepAdapter == null)
            stepAdapter = new StepListAdapter(
                    (StepListAdapter.StepListItemClickListener) getActivity(),
                    R.layout.item_step_list);
        rvStepList.setAdapter(stepAdapter);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StepListViewModel.MyFactory factory = new StepListViewModel.MyFactory(getActivity().getApplication(), recipeId);
        viewModel = ViewModelProviders.of(this, factory).get(StepListViewModel.class);
        registerObservers();
    }

    private void registerObservers() {
        viewModel.stepList.observe(this, new Observer<StepListForRecipe>() {
            @Override public void onChanged(@Nullable StepListForRecipe stepList) {
                if (stepList != null) {
                    stepAdapter.setData(stepList.steps);
                    ingredAdapter.setData(stepList.ingredients);
                    scrollView.scrollTo(scrollX, scrollY);
                    String stepListCaption = String.valueOf(stepList.steps.size()) + " steps to make " + stepList.name;
                    tvName.setText(stepListCaption);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(stepList.name);
                }
            }
        });
    }

}
