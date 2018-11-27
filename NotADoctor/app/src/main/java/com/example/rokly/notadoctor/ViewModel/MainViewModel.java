package com.example.rokly.notadoctor.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<UserEntry>> user;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        user = database.databaseDao().loadAllUser();
    }

    public LiveData<List<UserEntry>> getUsers() {
        return user;
    }
}
