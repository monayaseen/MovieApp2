package com.example.movieappstage2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movieappstage2.dataBase.DbApp;

public class MainView extends AndroidViewModel {

    private LiveData<Movie[]> movies;

    public MainView(@NonNull Application application) {
        super (application);
        DbApp database = DbApp.getInstance (this.getApplication ());
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<Movie[]> getMovies() {
        return movies;
    }
}

