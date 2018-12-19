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
import com.example.rokly.notadoctor.Model.Places.Result;
import com.example.rokly.notadoctor.R;
import com.example.rokly.notadoctor.helper.ButtonAnimator;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesAdapterViewHolder> {
    private static int expandedPosition = -1;
    private static int previousExpandedPosition = -1;
    private final PlacesAdapter.ItemClickListener clickHandler;
    private RecyclerView recyclerView;
    /* List for all user*/
    private List<Result> resultList;
    private Context context;
    private AnimatedVectorDrawable upToDown;

    public PlacesAdapter(PlacesAdapter.ItemClickListener clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacesAdapter.PlacesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.result_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new PlacesAdapter.PlacesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull PlacesAdapter.PlacesAdapterViewHolder forecastAdapterViewHolder,final int position) {
        /*Get the current doctor */
        final Result result = resultList.get(forecastAdapterViewHolder.getAdapterPosition());

        AnimatedVectorDrawable downToUp = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.avd_down_to_up);
        upToDown = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, R.drawable.avd_up_to_down);

        forecastAdapterViewHolder.resultNameTextView.setText(result.getName());

        final boolean isExpanded = forecastAdapterViewHolder.getAdapterPosition()==expandedPosition;
        forecastAdapterViewHolder.doctorConstraintLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        forecastAdapterViewHolder.itemView.setActivated(isExpanded);
        forecastAdapterViewHolder.doctorAdressTextView.setText(result.getFormattedAddress());
        forecastAdapterViewHolder.doctorTelephoneTextView.setText(result.getDetailResult().getFormattedPhoneNumber());
        ButtonAnimator.imageButtonAnimator(forecastAdapterViewHolder.callADoctor);

        if (isExpanded){
            forecastAdapterViewHolder.upDownView.setImageDrawable(downToUp);
            assert downToUp != null;
            downToUp.start();

            previousExpandedPosition = forecastAdapterViewHolder.getAdapterPosition();
        }

        forecastAdapterViewHolder.itemView.setOnClickListener(v -> {
            AnimatedVectorDrawable drawable = upToDown;
            forecastAdapterViewHolder.upDownView.setImageDrawable(drawable);
            drawable.start();

                clickHandler.onExpandClickListener(result.getName());
                expandedPosition = isExpanded ? -1:forecastAdapterViewHolder.getAdapterPosition();
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyItemChanged(previousExpandedPosition);
                notifyDataSetChanged();
        });

        forecastAdapterViewHolder.callADoctor.setOnClickListener(view -> clickHandler.onCallClickListener(result.getDetailResult().getFormattedPhoneNumber()));
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
        this.resultList = resultData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(Result currentResult);

        void onCallClickListener(String phoneNumber);

        void onExpandClickListener(String name);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    public class PlacesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView resultNameTextView;
        ConstraintLayout doctorConstraintLayout;
        TextView doctorAdressTextView;
        TextView doctorTelephoneTextView;
        ImageButton callADoctor;
        ImageView upDownView;

        PlacesAdapterViewHolder(View view) {
            super(view);
            resultNameTextView = view.findViewById(R.id.tv_list_result_name);
            doctorConstraintLayout = view.findViewById(R.id.result_detail_layout);
            doctorAdressTextView = view.findViewById(R.id.tv_result_address);
            doctorTelephoneTextView = view.findViewById(R.id.tv_result_phone_number);
            callADoctor = view.findViewById(R.id.ib_call_doctor);
            upDownView = view.findViewById(R.id.upside_down_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Result currentResult = resultList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentResult);
        }
    }
}