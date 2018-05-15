package com.and.tim.bakingapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.and.tim.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.rvRecipeList) RecyclerView rvRecipeList;
    @BindView(R.id.tvErrorMessage) TextView tvErrorMessage;
    @BindView(R.id.progressLoading) ProgressBar progressLoading;

    @BindInt(R.integer.smallest_screen_width_for_tablet) int SMALLEST_SCREEN_WIDTH;
    @BindString(R.string.network_error) String networkError;
    @BindString(R.string.label_loading) String labelLoading;

    private RecipeListViewModel viewModel;
    private RecipeListAdapter adapter;


    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, root);

        adapter = new RecipeListAdapter(
                (RecipeListAdapter.RecipeListItemClick) getActivity(),
                R.layout.item_recipe_list);
        recyclerViewSetup();

        return root;
    }

    private void recyclerViewSetup() {
        int ssw = getResources().getConfiguration().smallestScreenWidthDp;
        RecyclerView.LayoutManager layoutManager;
        if (ssw < SMALLEST_SCREEN_WIDTH) // If tablet then use Grid
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        else
            layoutManager = new GridLayoutManager(getActivity(), 2);

        rvRecipeList.setLayoutManager(layoutManager);
        rvRecipeList.setVerticalScrollBarEnabled(true);
        rvRecipeList.setAdapter(adapter);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(RecipeListViewModel.class);

        registerObservers();
    }

    private void registerObservers() {
        viewModel.data.observe(this, new Observer<List<RecipeEntity>>() {
            @Override public void onChanged(@Nullable List<RecipeEntity> recipes) {
                adapter.setData(recipes);
            }
        });

        viewModel.isLoading.observe(this, new Observer<Pair<RecipeListViewModel.LoadingState, String>>() {
            @Override
            public void onChanged(@Nullable Pair<RecipeListViewModel.LoadingState, String> state) {
                if (state != null && state.first != null) {
                    switch (state.first) {
                        case LOADING:
                            progressLoading.setVisibility(View.VISIBLE);
                            tvErrorMessage.setVisibility(View.GONE);
                            break;
                        case DONE:
                            progressLoading.setVisibility(View.GONE);
                            tvErrorMessage.setVisibility(View.GONE);
                            break;
                        case ERROR:
                            progressLoading.setVisibility(View.GONE);
                            tvErrorMessage.setTag(state.second);
                            tvErrorMessage.setText(networkError);
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
    }

    @OnClick ({R.id.tvErrorMessage})
    public void showFullErrorMessage(View v) {
        ((TextView) v).setText((CharSequence) v.getTag());
    }
}
