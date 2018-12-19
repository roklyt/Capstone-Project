package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.R;

import java.text.DecimalFormat;
import java.util.List;

public class ConditionsAdapter extends RecyclerView.Adapter<ConditionsAdapter.ConditionsAdapterViewHolder> {
    private static int expandedPosition = -1;
    private final ConditionsAdapter.ItemClickListener clickHandler;
    private RecyclerView recyclerView;
    /* List for all user*/
    private List<Condition> conditionList;
    private AnimatedVectorDrawable upToDown;
    private Context context;

    public ConditionsAdapter(ConditionsAdapter.ItemClickListener clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public ConditionsAdapter.ConditionsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.condition_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ConditionsAdapter.ConditionsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConditionsAdapter.ConditionsAdapterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final Condition condition = conditionList.get(forecastAdapterViewHolder.getAdapterPosition());

        AnimatedVectorDrawable downToUp = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.avd_down_to_up);
        upToDown = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.avd_up_to_down);


        forecastAdapterViewHolder.conditionNameTextView.setText(condition.getName());
        String percentage = new DecimalFormat("#.##").format(condition.getProbability() * 100);
        percentage = percentage + "%";

        forecastAdapterViewHolder.conditionProbabilityTextView.setText(percentage);

        final boolean isExpanded = forecastAdapterViewHolder.getAdapterPosition()==expandedPosition;
        forecastAdapterViewHolder.detailConstraintLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        forecastAdapterViewHolder.itemView.setActivated(isExpanded);

        final View view = forecastAdapterViewHolder.itemView;
        if (isExpanded){
            forecastAdapterViewHolder.upDownView.setImageDrawable(downToUp);

            assert downToUp != null;
            downToUp.start();


            clickHandler.onResetScreen(view);
        }


        forecastAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(!isExpanded){
                    clickHandler.onItemExpandChecklist(view, condition, forecastAdapterViewHolder.getAdapterPosition());
                }

                AnimatedVectorDrawable drawable = upToDown;
                forecastAdapterViewHolder.upDownView.setImageDrawable(drawable);
                drawable.start();
                expandedPosition = isExpanded ? -1:forecastAdapterViewHolder.getAdapterPosition();

                TransitionManager.beginDelayedTransition(recyclerView);
                notifyItemChanged(forecastAdapterViewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        forecastAdapterViewHolder.findADoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              clickHandler.onButtonClicked(condition);
            }
        });
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

        void onItemExpandChecklist(View view, Condition condition, int position);

        void onButtonClicked(Condition condition);

        void onResetScreen(View view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    public class ConditionsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView conditionNameTextView;
        TextView conditionProbabilityTextView;
        ConstraintLayout detailConstraintLayout;
        ImageButton findADoctorButton;
        ImageView upDownView;

        ConditionsAdapterViewHolder(View view) {
            super(view);
            conditionNameTextView = view.findViewById(R.id.tv_condition_name);
            conditionProbabilityTextView = view.findViewById(R.id.tv_condition_probability);
            detailConstraintLayout = view.findViewById(R.id.conditions_detail_layout);
            findADoctorButton = view.findViewById(R.id.bt_find_a_doctor);
            upDownView = view.findViewById(R.id.upside_down_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Condition currentMention = conditionList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentMention);
        }
    }
}