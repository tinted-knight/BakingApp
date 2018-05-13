package com.and.tim.bakingapp.ui.step_list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.base.BaseRecyclerViewAdapter;
import com.and.tim.bakingapp.base.BaseViewHolder;
import com.and.tim.bakingapp.repo.dao.IngredientEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends BaseRecyclerViewAdapter<
        IngredientsAdapter.IngredientsViewHolder, IngredientsAdapter.IngredItemClickListener, IngredientEntity> {

    public IngredientsAdapter(IngredItemClickListener listener, int itemLayoutId) {
        super(listener, itemLayoutId);
    }

    @NonNull @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(itemLayoutId, parent, false);

        return new IngredientsViewHolder(itemView);
    }

    public interface IngredItemClickListener {
        void onIngredItemClick();
    }

    class IngredientsViewHolder extends BaseViewHolder<IngredientEntity> {

        @BindView(R.id.cbName) CheckBox cbName;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onIngredItemClick();
                }
            });
        }

        @Override public void bind(IngredientEntity ingredient) {
            cbName.setText(ingredient.ingredient);
        }
    }

}
