package com.and.tim.bakingapp.ui.main;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.entities.RecipeEntity;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private List<RecipeEntity> data;
    private RecipeListItemClick listener;

    public RecipeListAdapter(RecipeListItemClick listener) {
        if (listener != null) this.listener = listener;
    }

    @NonNull @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_recipe_list, parent, false);

        return new RecipeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public void setData(List<RecipeEntity> newData) {
        if (newData == null) return;
        data = newData;
        this.notifyDataSetChanged();
    }

    public interface RecipeListItemClick {
        void onRecipeListItemClick(RecipeEntity recipe);
        void onPinRecipe(RecipeEntity recipe);
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.chipIngredients) TextView chipIngredients;
        @BindView(R.id.chipSteps) TextView chipSteps;
        @BindView(R.id.btnPin) ImageButton btnPin;
        @BindView(R.id.btnOpen) ImageButton btnOpen;

        @BindString(R.string.label_ingredients) String labelIngredients;
        @BindString(R.string.label_stets) String labelSteps;

        @BindDrawable(value = R.drawable.ic_bookmark_black_24dp, tint = R.attr.colorAccent)
        Drawable pinnedDrawable;
        @BindDrawable(value = R.drawable.ic_bookmark_border_black_24dp, tint = R.attr.colorPrimary)
        Drawable notPinnedDrawable;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onRecipeListItemClick(data.get(getAdapterPosition()));
            }
        };

        RecipeListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(clickListener);
            btnOpen.setOnClickListener(clickListener);
            btnPin.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onPinRecipe(data.get(getAdapterPosition()));
                }
            });

        }

        public void bind(RecipeEntity recipe) {
            tvName.setText(recipe.name);

            String textIngredients = String.valueOf(recipe.ingredientsCount) + " " + labelIngredients;
            String textSteps = String.valueOf(recipe.stepCount) + " " + labelSteps;
            chipIngredients.setText(textIngredients);
            chipSteps.setText(textSteps);

            if (recipe.pinned) {
                btnPin.setImageDrawable(pinnedDrawable);
            } else
                btnPin.setImageDrawable(notPinnedDrawable);
        }
    }

}
