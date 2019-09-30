package com.example.movieappstage2;
import com.example.movieappstage2.dataBase.DbApp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetilsView extends ViewModel {

    private LiveData<Movie> movie;

    public DetilsView(DbApp database, int movieId) {
        movie = database.movieDao ().loadMovieById (movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
