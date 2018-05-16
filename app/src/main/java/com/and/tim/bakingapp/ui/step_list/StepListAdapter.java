package com.and.tim.bakingapp.ui.step_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.repo.dao.entities.StepEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {

    private List<StepEntity> data;
    StepListItemClickListener listener;

    public StepListAdapter(StepListItemClickListener listener) {
        if (listener != null) this.listener = listener;
    }

    @NonNull @Override
    public StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_step_list, parent, false);

        return new StepListViewHolder(itemView);
    }

    @Override public void onBindViewHolder(@NonNull StepListViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.itemView.setTag(position);
    }

    @Override public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public void setData(List<StepEntity> newData) {
        if (newData == null) return;
        data = newData;
        this.notifyDataSetChanged();
    }

    public interface StepListItemClickListener {
        void onStepListItemClick(int data);
    }

    class StepListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvShortDescription) TextView tvShortDescription;
        @BindView(R.id.tvPosition) TextView tvPosition;

        StepListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onStepListItemClick((Integer) v.getTag());
                }
            });
        }

        public void bind(StepEntity step) {
            tvShortDescription.setText(step.getShortDescription());
            tvPosition.setText(String.valueOf(getAdapterPosition() + 1));
        }
    }
}
