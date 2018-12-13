package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Diagnose.Response.Condition;
import com.example.rokly.notadoctor.Model.PlaceDetail.PlaceDetail;
import com.example.rokly.notadoctor.Model.Places.Result;
import com.example.rokly.notadoctor.R;
import com.example.rokly.notadoctor.Retrofit.PlacesApi;
import com.example.rokly.notadoctor.Retrofit.RetrofitClientPlaces;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.internal.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesAdapterViewHolder> {
    private static int expandedPosition = -1;
    private static int previousExpandedPosition = -1;
    private final PlacesAdapter.ItemClickListener clickHandler;
    private RecyclerView recyclerView;
    /* List for all user*/
    private List<Result> resultList;
    private Context context;

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
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PlacesAdapter.PlacesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull PlacesAdapter.PlacesAdapterViewHolder forecastAdapterViewHolder,final int position) {
        /*Get the current doctor */
        final Result result = resultList.get(position);

        forecastAdapterViewHolder.resultNameTextView.setText(result.getName());

        final boolean isExpanded = position==expandedPosition;
        forecastAdapterViewHolder.doctorConstraintLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        forecastAdapterViewHolder.itemView.setActivated(isExpanded);
        forecastAdapterViewHolder.doctorAdressTextView.setText(result.getFormattedAddress());
        forecastAdapterViewHolder.doctorTelephoneTextView.setText(result.getDetailResult().getFormattedPhoneNumber());
        runEnterAnimation(forecastAdapterViewHolder.itemView);
        if (isExpanded)
            previousExpandedPosition = position;

        forecastAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    expandedPosition = isExpanded ? -1:position;
                    TransitionManager.beginDelayedTransition(recyclerView);
                    notifyItemChanged(previousExpandedPosition);
                    notifyDataSetChanged();
            }
        });

        forecastAdapterViewHolder.callADoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.onCallClickListener(result.getDetailResult().getFormattedPhoneNumber());
            }
        });
    }

    private void runEnterAnimation(View view) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        view.setTranslationY(height);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
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

        PlacesAdapterViewHolder(View view) {
            super(view);
            resultNameTextView = view.findViewById(R.id.tv_list_result_name);
            doctorConstraintLayout = view.findViewById(R.id.result_detail_layout);
            doctorAdressTextView = view.findViewById(R.id.tv_result_address);
            doctorTelephoneTextView = view.findViewById(R.id.tv_result_phone_number);
            callADoctor = view.findViewById(R.id.ib_call_doctor);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Result currentResult = resultList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentResult);
        }
    }
}