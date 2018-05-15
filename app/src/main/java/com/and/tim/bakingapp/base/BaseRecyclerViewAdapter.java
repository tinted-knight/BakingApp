package com.and.tim.bakingapp.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter
        <VH extends BaseViewHolder, L, I>
        extends RecyclerView.Adapter<VH> {

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

//    @NonNull @Override public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);


    @Override public void onBindViewHolder(@NonNull VH holder, int position) {
        currentItem = data.get(position);
        holder.bind(currentItem);
    }

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
