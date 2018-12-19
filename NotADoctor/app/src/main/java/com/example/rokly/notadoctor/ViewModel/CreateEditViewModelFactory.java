package com.example.rokly.notadoctor.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.rokly.notadoctor.Database.AppDatabase;

public class CreateEditViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase Userdb;
    private final int UserId;

    public CreateEditViewModelFactory(AppDatabase database, int userId) {
        Userdb = database;
        UserId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CreateEditViewModel(Userdb, UserId);
    }
}
