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

    private final ItemClickListener clickHandler;
    /* List for all user*/
    private List<UserEntry> userList;

    public UserAdapter(ItemClickListener clickHandler) {
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterViewHolder forecastAdapterViewHolder, int position) {
        /*Get the current user */
        final UserEntry user = userList.get(position);

        forecastAdapterViewHolder.userNameTextView.setText(user.getName());

        forecastAdapterViewHolder.deleteUserImageButton.setOnClickListener(view -> clickHandler.onDeleteClickListener(user));

        forecastAdapterViewHolder.editUserImageButton.setOnClickListener(view -> clickHandler.onEditClickListener(user, view));
    }

    @Override
    public int getItemCount() {
        if(userList == null){
            return 0;
        }
        return userList.size();
    }

    /* Set the new user list to the adapter */
    public void setUserData(List<UserEntry> userData) {
        userList = userData;
        notifyDataSetChanged();
    }

    /* Interface for the on click handler */
    public interface ItemClickListener {
        void onItemClickListener(UserEntry currentUser, View sharedView);

        void onDeleteClickListener(UserEntry currentUser);

        void onEditClickListener(UserEntry currentUser, View view);
    }

    public class UserAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userNameTextView;
        ImageButton deleteUserImageButton;
        ImageButton editUserImageButton;

        UserAdapterViewHolder(View view) {
            super(view);
            deleteUserImageButton = view.findViewById(R.id.ib_delete_user);
            editUserImageButton = view.findViewById(R.id.ib_edit_user);
            userNameTextView = view.findViewById(R.id.tv_list_user_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserEntry currentUser = userList.get(getAdapterPosition());
            clickHandler.onItemClickListener(currentUser, v);
        }
    }
}