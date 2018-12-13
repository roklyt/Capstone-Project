package com.example.rokly.notadoctor;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.rokly.notadoctor.Adapter.UserAdapter;
import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;
import com.example.rokly.notadoctor.Executor.AppExecutor;
import com.example.rokly.notadoctor.ViewModel.MainViewModel;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements UserAdapter.ItemClickListener{

    AppDatabase notADoctorDB;

    private UserAdapter userAdapter;
    private RecyclerView userRecyclerView;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity = this;
        userRecyclerView = findViewById(R.id.recyclerview_user);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        userAdapter = new UserAdapter(this);
        userRecyclerView.setAdapter(userAdapter);


        FloatingActionButton addUser = findViewById(R.id.fab_add_user);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserIntent = new Intent(WelcomeActivity.this, CreateEditActivity.class);
                startActivity(addUserIntent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            }
        });

        notADoctorDB = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getUsers().observe(this, new Observer<List<UserEntry>>() {
            @Override
            public void onChanged(@Nullable List<UserEntry> userEntries) {
                userAdapter.setUserData(userEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(UserEntry currentUser, View sharedView) {
        Intent symptomeIntent = new Intent(WelcomeActivity.this, SymptomActivity.class);
        symptomeIntent.putExtra(SymptomActivity.EXTRA_USER, currentUser);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this,
                sharedView,
                getResources().getString(R.string.transition_name))
                .toBundle();
        startActivity(symptomeIntent, bundle);
    }

    @Override
    public void onDeleteClickListener(final UserEntry currentUser) {

        final Resources res = getResources();

        new AlertDialog.Builder(this)
                .setTitle(res.getString(R.string.alert_dialog_delete_user_title, currentUser.getName()))
                .setMessage(res.getString(R.string.alert_dialog_delete_user_text))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                notADoctorDB.databaseDao().deleteUser(currentUser);

                            }
                        });
                        Toast.makeText(getApplicationContext(), res.getString(R.string.delete_user_toast, currentUser.getName()), Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onEditClickListener(UserEntry currentUser, View sharedView) {
        Intent editUserIntent = new Intent(WelcomeActivity.this, CreateEditActivity.class);
        editUserIntent.putExtra(CreateEditActivity.EXTRA_USER_ID, currentUser.getId());
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this,
                sharedView,
                getResources().getString(R.string.transition_name))
                .toBundle();

        startActivity(editUserIntent, bundle);
    }
}
