package com.and.tim.bakingapp.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.base.BaseListener;
import com.and.tim.bakingapp.base.BaseRecyclerViewAdapter;
import com.and.tim.bakingapp.base.BaseViewHolder;
import com.and.tim.bakingapp.model.Ingredient;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
public class RecipeListAdapter extends BaseRecyclerViewAdapter
        <RecipeListAdapter.RecipeListViewHolder, RecipeListAdapter.RecipeListItemClick, RecipeEntity> {

    public RecipeListAdapter(RecipeListItemClick listener, int itemLayoutId) {
        super(listener, itemLayoutId);
    }

    @NonNull @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(itemLayoutId, parent, false);

        return new RecipeListViewHolder(itemView);
    }


    public interface RecipeListItemClick extends BaseListener {
        //        void onRecipeListItemClick(int recipeId);
        void onRecipeListItemClick(RecipeEntity recipe);

        void onPinRecipe(RecipeEntity recipe);
    }

    class RecipeListViewHolder extends BaseViewHolder<RecipeEntity> {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.btnPin) Button btnPin;

        RecipeListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
//                    listener.onRecipeListItemClick((Integer) v.getTag());
                    listener.onRecipeListItemClick(data.get(getAdapterPosition()));
                }
            });

            btnPin.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onPinRecipe(data.get(getAdapterPosition()));
                }
            });
        }

        @Override public void bind(RecipeEntity recipe) {
            tvName.setText(recipe.name);
        }
    }

}
