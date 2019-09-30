package com.example.movieappstage2;



import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieappstage2.dataBase.DbApp;

@SuppressWarnings("unchecked")
public class DetailsViewFactory  extends ViewModelProvider.NewInstanceFactory {
    private final DbApp mDb;
    private final int mMovieId;

    public DetailsViewFactory(DbApp database, int movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DetilsView(mDb, mMovieId);
    }
}
