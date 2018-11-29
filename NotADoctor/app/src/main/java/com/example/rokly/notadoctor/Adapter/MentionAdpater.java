package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Mentions;
import com.example.rokly.notadoctor.R;

import java.util.List;

public class MentionAdpater extends RecyclerView.Adapter<MentionAdpater.MentionAdpaterViewHolder> {

    private final MentionAdpater.ItemClickListener ClickHandler;
    /* List for all user*/
    private List<Mentions> Mentions;

    public MentionAdpater(MentionAdpater.ItemClickListener clickHandler) {
        ClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MentionAdpater.MentionAdpaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.mention_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MentionAdpater.MentionAdpaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentionAdpater.MentionAdpaterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final Mentions mention = Mentions.get(position);


        forecastAdapterViewHolder.MentionNameTextView.setText(mention.getName());
        if(mention.getChoiceId().equals("present")){
            forecastAdapterViewHolder.MentionIndicatorImageView.setImageResource(android.R.drawable.checkbox_on_background);
        }else if(mention.getChoiceId().equals("absent")){
            forecastAdapterViewHolder.MentionIndicatorImageView.setImageResource(android.R.drawable.ic_delete);
        }else{
            forecastAdapterViewHolder.MentionIndicatorImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(Mentions == null){
            return 0;
        }
        return Mentions.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<Mentions> mentionData) {
        Mentions = mentionData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(Mentions mention);
    }

    public void removeItem(int position) {
        Mentions.remove(position);
        notifyItemRemoved(position);
    }

    public class MentionAdpaterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView MentionNameTextView;
        ImageView MentionIndicatorImageView;

        public MentionAdpaterViewHolder(View view) {
            super(view);
            MentionNameTextView = view.findViewById(R.id.tv_mention_list);
            MentionIndicatorImageView = view.findViewById(R.id.iv_indicator);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Mentions currentMention = Mentions.get(getAdapterPosition());
            ClickHandler.onItemClickListener(currentMention);
        }
    }
}