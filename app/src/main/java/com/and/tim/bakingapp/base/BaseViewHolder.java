package com.and.tim.bakingapp.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder<I> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(I item);

}
