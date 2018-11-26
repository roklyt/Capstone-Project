package com.example.rokly.notadoctor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.rokly.notadoctor.Adapter.UserAdapter;
import com.example.rokly.notadoctor.Data.User;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.ViewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements UserAdapter.UserAdapterOnClickHandler{

    AppDatabase UsersDb;

    private UserAdapter UserAdapter;
    private List<User> UserList = new ArrayList<>();
    private RecyclerView UserRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        UserRecyclerView = findViewById(R.id.recyclerview_user);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        UserAdapter = new UserAdapter(this);
        UserRecyclerView.setAdapter(UserAdapter);



        FloatingActionButton addUser = findViewById(R.id.fab_add_user);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserEntry userentry = new UserEntry("Klaus", "woman", 0 ,0, 0, 0,0);
                UsersDb.databaseDao().insertUser(userentry);
            }
        });

        UsersDb = AppDatabase.getInstance(getApplicationContext());
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<UserEntry>>() {
            @Override
            public void onChanged(@Nullable List<UserEntry> userEntries) {
                UserAdapter.setUserData(userEntries);
            }
        });
    }

    @Override
    public void onClick(UserEntry currentUser) {

    }
}
