package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Places.Result;
import com.example.rokly.notadoctor.R;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesAdapterViewHolder> {
    private static int expandedPosition = -1;
    private static int previousExpandedPosition = -1;
    private final PlacesAdapter.ItemClickListener clickHandler;
    private RecyclerView recyclerView;
    /* List for all user*/
    private List<Result> resultList;

    public PlacesAdapter(PlacesAdapter.ItemClickListener clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PlacesAdapter.PlacesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.result_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PlacesAdapter.PlacesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.PlacesAdapterViewHolder forecastAdapterViewHolder,final int position) {
        /*Get the current user */
        final Result result = resultList.get(position);

        forecastAdapterViewHolder.resultNameTextView.setText(result.getName());

        final boolean isExpanded = position==expandedPosition;
        forecastAdapterViewHolder.doctorConstraintLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        forecastAdapterViewHolder.itemView.setActivated(isExpanded);
        final View view = forecastAdapterViewHolder.itemView;
        if (isExpanded)
            previousExpandedPosition = position;

        forecastAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isExpanded){
                    clickHandler.onItemExpandChecklist(view, result, position);
                }
                expandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyItemChanged(previousExpandedPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(resultList == null){
            return 0;
        }
        return resultList.size();
    }

    /* Set the new user list to the adapter */
    public void setPlacesData(List<Result> resultData) {
        resultList = resultData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(Result currentResult);

        void onItemExpandChecklist(View view, Result result, int position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    public class PlacesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView resultNameTextView;
        ConstraintLayout doctorConstraintLayout;

        PlacesAdapterViewHolder(View view) {
            super(view);
            resultNameTextView = view.findViewById(R.id.tv_list_result_name);
            doctorConstraintLayout = view.findViewById(R.id.conditions_detail_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Result currentResult = resultList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentResult);
        }
    }
}