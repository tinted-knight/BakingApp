package com.and.tim.bakingapp.ui.step_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private StepListAdapter adapter;
    private StepListViewModel viewModel;

    //Binds
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.rvStepList) RecyclerView rvStepList;

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

        populateStepList();

        return root;
    }

    private void populateStepList() {
        rvStepList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvStepList.addItemDecoration(new DividerItemDecoration(
                getActivity(),
                DividerItemDecoration.VERTICAL
        ));
        rvStepList.setVerticalScrollBarEnabled(true);

        adapter = new StepListAdapter((StepListAdapter.StepListItemClickListener) getActivity());
        rvStepList.setAdapter(adapter);
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
                adapter.setData(stepList.steps);
                tvName.setText(stepList.name);
            }
        });
    }
}
