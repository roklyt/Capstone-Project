package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Condition.ConditionDetail;
import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.Parse.Response.Mention;
import com.example.rokly.notadoctor.R;
import com.example.rokly.notadoctor.Retrofit.InfermedicaApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientInstance;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConditionsAdapter extends RecyclerView.Adapter<ConditionsAdapter.ConditionsAdapterViewHolder> {
    private static int expandedPosition = -1;
    private static int previousExpandedPosition = -1;
    private final ConditionsAdapter.ItemClickListener clickHandler;
    private RecyclerView recyclerView;
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
    public void onBindViewHolder(@NonNull final ConditionsAdapter.ConditionsAdapterViewHolder forecastAdapterViewHolder, final int position) {
        /*Get the current user */
        final Condition condition = conditionList.get(position);


        forecastAdapterViewHolder.conditionNameTextView.setText(condition.getName());
        String percentage = new DecimalFormat("#.##").format(condition.getProbability() * 100);
        forecastAdapterViewHolder.conditionProbabilityTextView.setText(percentage + "%");

        final boolean isExpanded = position==expandedPosition;
        forecastAdapterViewHolder.detailConstraintLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        forecastAdapterViewHolder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        forecastAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isExpanded){
                    InfermedicaApi infermedicaApi = RetrofitClientInstance.getRetrofitInstance().create(InfermedicaApi.class);

                    Call<ConditionDetail> call = infermedicaApi.getConditionById(condition.getId());
                    call.enqueue(new Callback<ConditionDetail>() {

                        @Override
                        public void onResponse(@NonNull Call<ConditionDetail> call, @NonNull Response<ConditionDetail> response) {
                            //TODO check for bad response
                            forecastAdapterViewHolder.conditionDetailsNameTextView.setText(response.body().getCommonName());
                            forecastAdapterViewHolder.conditionDetailsCategoriesTextView.setText(getCategories(response.body()));
                            forecastAdapterViewHolder.conditionDetailsPrevalenceTextView.setText(response.body().getPrevalence());
                            forecastAdapterViewHolder.conditionDetailsAcutenessTextView.setText(response.body().getAcuteness());
                            forecastAdapterViewHolder.conditionDetailsNameSeverityView.setText(response.body().getSeverity());
                            forecastAdapterViewHolder.conditionDetailsHintTextView.setText(response.body().getExtras().getHint());

                            response.body().getExtras().describeContents();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ConditionDetail> call, @NonNull Throwable t) {
                            Log.e("ConditionActivity","" +  t);

                        }
                    });
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

    private String getCategories(ConditionDetail conditionDetail){
        String categories = "";
        for(int i = 0;i < conditionDetail.getCategories().size();i++){
            if(i == 0){
                categories = conditionDetail.getCategories().get(i);
            }else{
                categories = categories + ", "+ conditionDetail.getCategories().get(i);
            }
        }

        return categories;
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
        TextView conditionDetailsNameTextView;
        TextView conditionDetailsCategoriesTextView;
        TextView conditionDetailsPrevalenceTextView;
        TextView conditionDetailsAcutenessTextView;
        TextView conditionDetailsNameSeverityView;
        TextView conditionDetailsHintTextView;

        ConditionsAdapterViewHolder(View view) {
            super(view);
            conditionNameTextView = view.findViewById(R.id.tv_condition_name);
            conditionProbabilityTextView = view.findViewById(R.id.tv_condition_probability);
            detailConstraintLayout = view.findViewById(R.id.conditions_detail_layout);
            conditionDetailsNameTextView = view.findViewById(R.id.tv_name_value);
            conditionDetailsCategoriesTextView = view.findViewById(R.id.tv_categories_value);
            conditionDetailsPrevalenceTextView = view.findViewById(R.id.tv_prevalence_value);
            conditionDetailsAcutenessTextView = view.findViewById(R.id.tv_acuteness_value);
            conditionDetailsNameSeverityView = view.findViewById(R.id.tv_severity_value);
            conditionDetailsHintTextView = view.findViewById(R.id.tv_hint);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Condition currentMention = conditionList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentMention);
        }
    }
}