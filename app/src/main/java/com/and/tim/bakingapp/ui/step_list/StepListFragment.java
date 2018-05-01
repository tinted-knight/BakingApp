package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_list, container, false);
//        View root = inflater.inflate(R.layout.card_step_list, container, false);
        ButterKnife.bind(this, root);

        setupStepList();
        setupIngredientList();

        return root;
    }

    private void setupIngredientList() {
        rvIngredList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvIngredList.setVerticalScrollBarEnabled(true);

        if (ingredAdapter == null)
            ingredAdapter = new IngredientsAdapter(
                    (IngredientsAdapter.IngredItemClickListener) getActivity(),
                    R.layout.item_ingredient);
        rvIngredList.setAdapter(ingredAdapter);

        btnExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (cardStateExpanded) {
                    TransitionManager.beginDelayedTransition(layoutSteps);
                    rvIngredList.setVisibility(View.GONE);
                    cardStateExpanded = false;
                    btnExpandCollapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                } else { // is collapsed
                    TransitionManager.beginDelayedTransition(layoutSteps);
                    rvIngredList.setVisibility(View.VISIBLE);
                    cardStateExpanded = true;
                    btnExpandCollapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
                }
            }
        });
    }

    private void setupStepList() {
        rvStepList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
//        rvStepList.addItemDecoration(new DividerItemDecoration(
//                getActivity(),
//                DividerItemDecoration.VERTICAL
//        ));
//        rvStepList.setVerticalScrollBarEnabled(true);
        if (stepAdapter == null)
            stepAdapter = new StepListAdapter(
                    (StepListAdapter.StepListItemClickListener) getActivity(),
                    R.layout.item_step_list);
        rvStepList.setAdapter(stepAdapter);
    }

    @Override public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(StepListViewModel.class);
        registerObservers();
    }

    private void registerObservers() {
        viewModel.stepList.observe(this, new Observer<StepListForRecipe>() {
            @Override public void onChanged(@Nullable StepListForRecipe stepList) {
                stepAdapter.setData(stepList != null ? stepList.steps : null);
                ingredAdapter.setData(stepList != null ? stepList.ingredients : null);
                tvName.setText(stepList != null ? stepList.name : getString(R.string.common_error));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(stepList.name);
            }
        });
    }
}
