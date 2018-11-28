package com.example.rokly.notadoctor.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

    private final ItemClickListener ClickHandler;
    /* List for all user*/
    private List<UserEntry> UserList;

    public UserAdapter(ItemClickListener clickHandler) {
        ClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final UserEntry user = UserList.get(position);

        forecastAdapterViewHolder.UserNameTextView.setText(user.getName());

        forecastAdapterViewHolder.DeleteUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickHandler.onDeleteClickListener(user);
            }
        });

        forecastAdapterViewHolder.EditUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickHandler.onEditClickListener(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(UserList == null){
            return 0;
        }
        return UserList.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<UserEntry> userData) {
        UserList = userData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(UserEntry currentUser);

        void onDeleteClickListener(UserEntry currentUser);

        void onEditClickListener(UserEntry currentUser);
    }

    public class UserAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView UserNameTextView;
        ImageButton DeleteUserImageButton;
        ImageButton EditUserImageButton;

        public UserAdapterViewHolder(View view) {
            super(view);
            DeleteUserImageButton = view.findViewById(R.id.ib_delete_user);
            EditUserImageButton = view.findViewById(R.id.ib_edit_user);
            UserNameTextView = view.findViewById(R.id.tv_list_user_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserEntry currentUser = UserList.get(getAdapterPosition());
            ClickHandler.onItemClickListener(currentUser);
        }
    }
}