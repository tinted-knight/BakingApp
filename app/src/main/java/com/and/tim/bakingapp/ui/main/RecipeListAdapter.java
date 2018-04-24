package com.and.tim.bakingapp.ui.main;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.base.BaseListener;
import com.and.tim.bakingapp.base.BaseRecyclerViewAdapter;
import com.and.tim.bakingapp.base.BaseViewHolder;
import com.and.tim.bakingapp.model.Ingredient;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;
import com.robertlevonyan.views.chip.Chip;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        @BindView(R.id.chipIngredients) Chip chipIngredients;
        @BindView(R.id.chipSteps) Chip chipSteps;
        @BindView(R.id.btnPin) ImageButton btnPin;
        @BindView(R.id.btnOpen) ImageButton btnOpen;
//        @BindView(R.id.imgImage) ImageView imgImage;

        @BindString(R.string.label_ingredients) String labelIngredients;
        @BindString(R.string.label_stets) String labelSteps;

        @BindDrawable(value = R.drawable.ic_bookmark_black_24dp, tint = R.attr.colorAccent) Drawable pinnedDrawable;
        @BindDrawable(value = R.drawable.ic_bookmark_border_black_24dp, tint = R.attr.colorPrimary) Drawable notPinnedDrawable;

//        @BindDrawable(R.drawable.nutella) Drawable nutella;
//        @BindDrawable(R.drawable.brownies) Drawable brownies;
//        @BindDrawable(R.drawable.yellow_cake) Drawable yellowCake;
//        @BindDrawable(R.drawable.cheesecake) Drawable cheesecake;

        RecipeListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onRecipeListItemClick(data.get(getAdapterPosition()));
                }
            });

            btnPin.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onPinRecipe(data.get(getAdapterPosition()));
                }
            });

            btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onRecipeListItemClick(data.get(getAdapterPosition()));
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

//            switch (recipe.name.toLowerCase()) {
//                case "nutella pie":
//                    imgImage.setImageDrawable(nutella);
//                    break;
//                case "brownies":
//                    imgImage.setImageDrawable(brownies);
//                    break;
//                case "yellow cake":
//                    imgImage.setImageDrawable(yellowCake);
//                    break;
//                case "cheesecake":
//                    imgImage.setImageDrawable(cheesecake);
//                    break;
//            }

        }
    }

}
