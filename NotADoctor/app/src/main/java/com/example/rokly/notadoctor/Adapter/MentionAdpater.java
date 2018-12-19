package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rokly.notadoctor.Model.Parse.Response.Mentions;
import com.example.rokly.notadoctor.R;

import java.util.List;

import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_ABSENT;
import static com.example.rokly.notadoctor.helper.ChoiceId.CHOICEID_PRESENT;

public class MentionAdpater extends RecyclerView.Adapter<MentionAdpater.MentionAdpaterViewHolder> {

    private final MentionAdpater.ItemClickListener clickHandler;
    /* List for all user*/
    private List<Mentions> mentionsList;

    public MentionAdpater(MentionAdpater.ItemClickListener clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MentionAdpater.MentionAdpaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.mention_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MentionAdpater.MentionAdpaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentionAdpater.MentionAdpaterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final Mentions mention = mentionsList.get(position);


        forecastAdapterViewHolder.mentionNameTextView.setText(mention.getName());
        switch (mention.getChoiceId()) {
            case CHOICEID_PRESENT:
                forecastAdapterViewHolder.mentionIndicatorImageView.setImageResource(android.R.drawable.checkbox_on_background);
                break;
            case CHOICEID_ABSENT:
                forecastAdapterViewHolder.mentionIndicatorImageView.setImageResource(android.R.drawable.ic_delete);
                break;
            default:
                forecastAdapterViewHolder.mentionIndicatorImageView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mentionsList == null){
            return 0;
        }
        return mentionsList.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<Mentions> mentionData) {
        mentionsList = mentionData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(Mentions mention);
    }

    public void removeItem(int position) {
        mentionsList.remove(position);
        notifyItemRemoved(position);
    }

    public class MentionAdpaterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mentionNameTextView;
        ImageView mentionIndicatorImageView;

        MentionAdpaterViewHolder(View view) {
            super(view);
            mentionNameTextView = view.findViewById(R.id.tv_mention_list);
            mentionIndicatorImageView = view.findViewById(R.id.iv_indicator);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Mentions currentMention = mentionsList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentMention);
        }
    }
}