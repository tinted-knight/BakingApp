package com.and.tim.bakingapp.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.model.Ingredient;
import com.and.tim.bakingapp.model.Recipe;
import com.and.tim.bakingapp.repo.dao.RecipeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private List<RecipeEntity> data;
    private RecipeListItemClick listener;

    public RecipeListAdapter(RecipeListItemClick listener) {
        if (listener != null) this.listener = listener;
    }

    public void setData(List<RecipeEntity> newData) {
        if (newData == null) return;
        data = newData;
        this.notifyDataSetChanged();
    }

    @NonNull @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_recipe_list, parent, false);

        return new RecipeListViewHolder(itemView);
    }

    @Override public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        RecipeEntity recipe = data.get(position);
        holder.bind(recipe);
        holder.itemView.setTag(recipe.id);
    }

    @Override public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public interface RecipeListItemClick {
//        void onRecipeListItemClick(int recipeId);
        void onRecipeListItemClick(RecipeEntity recipe);
        void onPinRecipe(RecipeEntity recipe);
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder {

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

        void bind(RecipeEntity recipe) {
            tvName.setText(recipe.name);
        }

    }

}
