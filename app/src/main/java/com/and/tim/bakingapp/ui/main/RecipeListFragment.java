package com.and.tim.bakingapp.ui.main;

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

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.rvRecipeList) RecyclerView rvRecipeList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecipeListViewModel viewModel;
    private RecipeListAdapter adapter;


    public RecipeListFragment() {
        // Required empty public constructor
    }

    public static RecipeListFragment newInstance(String param1, String param2) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, root);

        adapter = new RecipeListAdapter((RecipeListAdapter.RecipeListItemClick) getActivity());
        recyclerViewSetup();

        return root;
    }

    private void recyclerViewSetup() {
        rvRecipeList.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        rvRecipeList.addItemDecoration(new DividerItemDecoration(
                getActivity(),
                DividerItemDecoration.VERTICAL
        ));
        rvRecipeList.setVerticalScrollBarEnabled(true);
        rvRecipeList.setAdapter(adapter);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(RecipeListViewModel.class);
        viewModel.init();

        registerObservers();
    }

    private void registerObservers() {
        viewModel.data.observe(this, new Observer<List<RecipeEntity>>() {
            @Override public void onChanged(@Nullable List<RecipeEntity> recipes) {
                adapter.setData(recipes);
            }
        });
    }
}
