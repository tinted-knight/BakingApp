package com.and.tim.bakingapp.ui.main;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.base.BaseRecyclerViewAdapter;
import com.and.tim.bakingapp.base.BaseViewHolder;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.robertlevonyan.views.chip.Chip;

import butterknife.BindDrawable;
import butterknife.BindString;
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

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    public interface RecipeListItemClick {
        void onRecipeListItemClick(RecipeEntity recipe);
        void onPinRecipe(RecipeEntity recipe);
    }

    class RecipeListViewHolder extends BaseViewHolder<RecipeEntity> {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.chipIngredients) Chip chipIngredients;
        @BindView(R.id.chipSteps) Chip chipSteps;
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

        @Override public void bind(RecipeEntity recipe) {
            tvName.setText(recipe.name);

            String textIngredients = String.valueOf(recipe.ingredientsCount) + " " + labelIngredients;
            String textSteps = String.valueOf(recipe.stepCount) + " " + labelSteps;
            chipIngredients.setChipText(textIngredients);
            chipSteps.setChipText(textSteps);

            if (recipe.pinned) {
                btnPin.setImageDrawable(pinnedDrawable);
            } else
                btnPin.setImageDrawable(notPinnedDrawable);
        }
    }

}
