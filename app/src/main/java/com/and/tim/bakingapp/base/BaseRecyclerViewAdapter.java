package com.and.tim.bakingapp.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseRecyclerViewAdapter
        <H extends BaseViewHolder, L extends BaseListener, I extends Marker>
        extends RecyclerView.Adapter<H> {

    protected L listener;
    protected List<I> data;
    protected int itemLayoutId;
    protected I currentItem;

    public BaseRecyclerViewAdapter(L listener, int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        if (listener != null) {
            this.listener = listener;
        }
    }

    @NonNull @Override public abstract H onCreateViewHolder(@NonNull ViewGroup parent, int viewType);


    @Override public void onBindViewHolder(@NonNull H holder, int position) {
//        I item = data.get(position);
        currentItem = data.get(position);
        holder.bind(currentItem);
    };

    @Override public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public void setData(List<I> newData) {
        if (newData == null) return;
        data = newData;
        this.notifyDataSetChanged();
    }

}
