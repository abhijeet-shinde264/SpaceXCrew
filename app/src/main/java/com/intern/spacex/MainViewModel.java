package com.intern.spacex;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    Repository repository;
    AppDao dao;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        dao = AppDatabase.getInstance(application).dao();
    }

    public LiveData<List<CrewMember>> getAllCrewMember() {
        return dao.getAllCrewMember();
    }

    public void refreshData() {
        repository.fetchCrewMember();
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }

    public void setResponseListener(ResponseCallback listener) {
        repository.setCallback(listener);
    }
}
