package com.example.rokly.notadoctor.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.rokly.notadoctor.Database.AppDatabase;
import com.example.rokly.notadoctor.Database.UserEntry;

public class CreateEditViewModel extends ViewModel {

    private LiveData<UserEntry> user;

    CreateEditViewModel(AppDatabase database, int userId) {
        user = database.databaseDao().loadUserById(userId);
    }

    public LiveData<UserEntry> getUser() {
        return user;
    }
}
