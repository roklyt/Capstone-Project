package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.R;

import java.text.DecimalFormat;
import java.util.List;

public class ConditionsAdapter extends RecyclerView.Adapter<ConditionsAdapter.ConditionsAdapterViewHolder> {

    private final ConditionsAdapter.ItemClickListener clickHandler;
    /* List for all user*/
    private List<Condition> conditionList;
    public ConditionsAdapter(ConditionsAdapter.ItemClickListener clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ConditionsAdapter.ConditionsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.condition_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ConditionsAdapter.ConditionsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConditionsAdapter.ConditionsAdapterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final Condition condition = conditionList.get(position);


        forecastAdapterViewHolder.conditionNameTextView.setText(condition.getName());
        String percentage = new DecimalFormat("#.##").format(condition.getProbability() * 100);
        forecastAdapterViewHolder.conditionProbabilityTextView.setText(percentage + "%");

    }

    @Override
    public int getItemCount() {
        if(conditionList == null){
            return 0;
        }
        return conditionList.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<Condition> conditionList) {
        this.conditionList = conditionList;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(Condition condition);
    }

    public void removeItem(int position) {
        conditionList.remove(position);
        notifyItemRemoved(position);
    }

    public class ConditionsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView conditionNameTextView;
        TextView conditionProbabilityTextView;

        public ConditionsAdapterViewHolder(View view) {
            super(view);
            conditionNameTextView = view.findViewById(R.id.tv_condition_name);
            conditionProbabilityTextView = view.findViewById(R.id.tv_condition_probability);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Condition currentMention = conditionList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentMention);
        }
    }
}