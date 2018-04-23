package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.StepListForRecipe;
import com.and.tim.bakingapp.viewmodel.StepListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {

    private static final String ARG_PARAM2 = "recipe_id";

    // TODO: Rename and change types of parameters
    private Recipe recipe;
    private int recipeId;
    private boolean cardStateExpanded = false;

    private StepListAdapter stepAdapter;
    private IngredientsAdapter ingredAdapter;

    private StepListViewModel viewModel;

    //Binds
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.rvStepList) RecyclerView rvStepList;
    @BindView(R.id.rvIngredList) RecyclerView rvIngredList;
    @BindView(R.id.btnExpandCollapse) ImageButton btnExpandCollapse;
    @BindView(R.id.layoutSteps) LinearLayout layoutSteps;

    public StepListFragment() {
        // Required empty public constructor
    }

    public static StepListFragment newInstance(int recipeId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, root);

        setupStepList();
        setupIngredientList();

        btnExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (cardStateExpanded) {
//                    TransitionManager.beginDelayedTransition(cardViewIngredients);
                    TransitionManager.beginDelayedTransition(layoutSteps);
                    rvIngredList.setVisibility(View.GONE);
                    cardStateExpanded = false;
                } else { // is collapsed
//                    TransitionManager.beginDelayedTransition(cardViewIngredients);
                    TransitionManager.beginDelayedTransition(layoutSteps);
                    rvIngredList.setVisibility(View.VISIBLE);
                    cardStateExpanded = true;
                }
            }
        });

        return root;
    }

    private void setupIngredientList() {
        rvIngredList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvIngredList.setVerticalScrollBarEnabled(true);

        ingredAdapter = new IngredientsAdapter(
                (IngredientsAdapter.IngredItemClickListener) getActivity(),
                R.layout.item_ingredient);
        rvIngredList.setAdapter(ingredAdapter);
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
//        rvStepList.setVerticalScrollBarEnabled(true);

        stepAdapter = new StepListAdapter(
                (StepListAdapter.StepListItemClickListener) getActivity(),
                R.layout.item_step_list);
        rvStepList.setAdapter(stepAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(StepListViewModel.class);
        viewModel.init(recipeId);
        registerObservers();
    }

    private void registerObservers() {
        viewModel.stepList.observe(this, new Observer<StepListForRecipe>() {
            @Override public void onChanged(@Nullable StepListForRecipe stepList) {
                stepAdapter.setData(stepList.steps);
                ingredAdapter.setData(stepList.ingredients);
                tvName.setText(stepList.name);
            }
        });
    }
}
