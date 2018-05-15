package com.and.tim.bakingapp.ui.step_list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.and.tim.bakingapp.R;
import com.and.tim.bakingapp.base.BaseRecyclerViewAdapter;
import com.and.tim.bakingapp.base.BaseViewHolder;
import com.and.tim.bakingapp.repo.dao.StepEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

//public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {
public class StepListAdapter extends BaseRecyclerViewAdapter<
        StepListAdapter.StepListViewHolder, StepListAdapter.StepListItemClickListener, StepEntity> {

    public StepListAdapter(StepListItemClickListener listener, int itemLayoutId) {
        super(listener, itemLayoutId);
    }

    @NonNull @Override
    public StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(itemLayoutId, parent, false);

        return new StepListViewHolder(itemView);
    }

    @Override public void onBindViewHolder(@NonNull StepListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//        holder.itemView.setTag(currentItem._id);
        holder.itemView.setTag(position);
    }

    public interface StepListItemClickListener {
        void onTest(int data);
    }

    class StepListViewHolder extends BaseViewHolder<StepEntity> {

        @BindView(R.id.tvShortDescription) TextView tvShortDescription;
        @BindView(R.id.tvPosition) TextView tvPosition;

        StepListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onTest((Integer) v.getTag());
                }
            });
        }

        @Override public void bind(StepEntity step) {
            tvShortDescription.setText(step.getShortDescription());
            tvPosition.setText(String.valueOf(getAdapterPosition() + 1));
        }
    }
}
