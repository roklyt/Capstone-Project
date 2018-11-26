package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.rokly.notadoctor.Data.User;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

    private final UserAdapterOnClickHandler ClickHandler;
    /* List for all user*/
    private List<UserEntry> UserList;

    public UserAdapter(UserAdapterOnClickHandler clickHandler) {
        ClickHandler = clickHandler;
    }

    @Override
    public UserAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        UserEntry user = UserList.get(position);

        forecastAdapterViewHolder.UserNameTextView.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<UserEntry> userData) {
        UserList = userData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface UserAdapterOnClickHandler {
        void onClick(UserEntry currentUser);
    }

    public class UserAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView UserNameTextView;

        public UserAdapterViewHolder(View view) {
            super(view);
            UserNameTextView = view.findViewById(R.id.tv_list_user_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserEntry currentUser = UserList.get(getAdapterPosition());
            ClickHandler.onClick(currentUser);
        }
    }
}