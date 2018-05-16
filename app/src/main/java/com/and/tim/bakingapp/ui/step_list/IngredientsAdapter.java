package com.and.tim.bakingapp.ui.step_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.entities.IngredientEntity;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private List<IngredientEntity> data;

    @NonNull @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_ingredient, parent, false);

        return new IngredientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    public void setData(List<IngredientEntity> newData) {
        if (newData == null) return;
        data = newData;
        this.notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvQuantity) TextView tvQuantity;
        @BindColor(R.color.list_light_row) int colorLightRow;
        @BindColor(R.color.list_dark_row) int colorDarkRow;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(IngredientEntity ingredient) {
            if (getAdapterPosition() % 2 != 0)
                itemView.setBackgroundColor(colorDarkRow);
            else
                itemView.setBackgroundColor(colorLightRow);

            tvName.setText(ingredient.ingredient);
            String quantity = ingredient.quantity + " " + ingredient.measure;
            tvQuantity.setText(quantity);
        }
    }

}
