package com.example.movieappstage2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieappstage2.dataBase.DbApp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbApp mDb;
    private int selectedItem;
    private MenuItem menuItem;
    private Movie[] movies;
    private MovieAdapter mImageAdapter;
    private Parcelable mListState;

    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        mRecyclerView = findViewById (R.id.recycler_view);

        // Using a Grid Layout Manager
        mLayoutManager = new GridLayoutManager(this,3);

        mRecyclerView.getRecycledViewPool ().clear ();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDb = DbApp.getInstance (getApplicationContext ());

        if(savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt("OPTION");
        }

        //Default to Popular Query Sort
        new FetchDataAsyncTask().execute(Const.POPULAR_QUERY_PARAM);
            // Check if online
            if (isOnline()) {

                switch (selectedItem) {
                    case R.id.popular_setting:
                        //Default to Popular Query Sort
                        new FetchDataAsyncTask().execute(Const.POPULAR_QUERY_PARAM);
                        break;


                    case R.id.top_rated_setting:
                        //Default to Popular Query Sort
                        new FetchDataAsyncTask().execute(Const.TOP_RATED_QUERY_PARAM);
                        break;

                    case R.id.favorite_movie_setting:
                        //Default to Popular Query Sort
                        new FetchDataAsyncTask().execute("favorite");
                        break;
                }
            } else {
                Toast.makeText(getApplicationContext(), Const.NO_INTERNET_TEXT, Const.TOAST_DURATION).show();
            }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);

        outState.putInt("OPTION", selectedItem);
        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("LIST_STATE_KEY", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        selectedItem = outState.getInt ("OPTION");

        // Retrieve list state and list/item positions
        if(outState != null)
            mListState = outState.getParcelable("LIST_STATE_KEY");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    //Attempt to try and save selected menu item on screen rotation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate(R.menu.main, menu);
        switch (selectedItem){
            case R.id.popular_setting:
                menuItem = menu.findItem(R.id.popular_setting);
               // menuItem.setChecked (true);
                break;

            case R.id.top_rated_setting:
                menuItem = menu.findItem(R.id.top_rated_setting);
                menuItem.setChecked (true);
                break;

            case R.id.favorite_movie_setting:
                menuItem = menu.findItem(R.id.popular_setting);
                menuItem.setChecked (true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.popular_setting) {
            selectedItem = id;
            item.setVisible (true);
            new FetchDataAsyncTask ().execute(Const.POPULAR_QUERY_PARAM);
            return true;
        }
        if (id == R.id.top_rated_setting) {
            selectedItem = id;
            item.setVisible (true);
            new FetchDataAsyncTask().execute(Const.TOP_RATED_QUERY_PARAM);
            return true;
        }
        if (id == R.id.favorite_movie_setting){
            selectedItem = id;
            item.setVisible (true);
            setUpViewModel (); // Favorite Movies
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    public void setUpViewModel() {
        MainView viewModel = ViewModelProviders.of(this).get(MainView.class);
        viewModel.getMovies().observe (this, new Observer<Movie[]>() {
            @Override
            public void onChanged(Movie[] movies1) {
                mImageAdapter.notifyDataSetChanged();
                mImageAdapter.setMovies(movies1);
            }
        });
    }
    public Movie[] makeMoviesDataToArray(String moviesJsonResults) throws JSONException {

        // Get results as an array
        JSONObject moviesJson = new JSONObject(moviesJsonResults);
        JSONArray resultsArray = moviesJson.getJSONArray(Const.RESULTS_QUERY_PARAM);

        // Create array of Movie objects that stores data from the JSON string
        movies = new Movie[resultsArray.length()];

        // Go through movies one by one and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            movies[i] = new Movie();

            // Object contains all tags we're looking for
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            // Store data in movie object
            movies[i].setOriginalTitle(movieInfo.getString(Const.ORIGINAL_TITLE_QUERY_PARAM));
            movies[i].setPosterPath(Const.MOVIEDB_IMAGE_BASE_URL + movieInfo.getString(Const.POSTER_PATH_QUERY_PARAM));
            movies[i].setOverview(movieInfo.getString(Const.OVERVIEW_QUERY_PARAM));
            movies[i].setVoterAverage(movieInfo.getDouble(Const.VOTER_AVERAGE_QUERY_PARAM));
            movies[i].setReleaseDate(movieInfo.getString(Const.RELEASE_DATE_QUERY_PARAM));
            movies[i].setMovieId (movieInfo.getInt (Const.MOVIE_ID_QUERY_PARAM));
        }
        return movies;
    }

    /*** FETCH MOVIE DATA ASYNC TASK ***/
    public class FetchDataAsyncTask extends AsyncTask<String, Void, Movie[]> {
        public FetchDataAsyncTask() {
            super();
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            // Holds data returned from the API
            String movieSearchResults;

            try {
                URL url = JSON.buildUrl(params);
                movieSearchResults = JSON.getResponseFromHttpUrl(url);

                if(movieSearchResults == null) {
                    return null;
                }
                return makeMoviesDataToArray (movieSearchResults);
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                e.printStackTrace ();
            }
            return null;
        }

        protected void onPostExecute(Movie[] movies) {
            mImageAdapter = new MovieAdapter(getApplicationContext(), movies);
            mRecyclerView.setAdapter(mImageAdapter);
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec(Const.INTERNET_CHECK_COMMAND);
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}