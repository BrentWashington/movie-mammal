package com.apps.washington.moviemammal;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apps.washington.moviemammal.adapter.MovieAdapter;
import com.apps.washington.moviemammal.constructor.Movie;
import com.apps.washington.moviemammal.loader.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * URL for movie data
     */
    private static final String POPULAR_MOVIES_REQUEST_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=4c7d91acb8258c53699d77462b580a07&language=en-US&page=1";

    /**
     * Constant value for the movie loader ID
     */
    private static final int MOVIE_LOADER_ID = 1;

    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;

    /**
     * The GridView to be populated with movie data
     */
    private GridView mGridView;

    /**
     * The progress bar
     */
    private ProgressBar mProgressBar;

    /**
     * ConnectivityManager for checking for internet connection
     */
    private ConnectivityManager mConnectivityManager;

    /**
     * Empty state TextView
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the TextView with the ID "emptyStateTextView"
        mEmptyStateTextView = findViewById(R.id.emptyStateTextView);

        // Find the Progress bar with the ID "progressBar"
        mProgressBar = findViewById(R.id.progressBar);

        // Find the ListView with the ID "list"
        mGridView = findViewById(R.id.grid);

        // Create a new adapter that takes an empty list of movies as input
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the ListView so the list can be populated
        mGridView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check the state of network connectivity
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

        // If there is a network connection, execute the Loader to fetch the data
        if (networkInfo != null && networkInfo.isConnected()) {
            /*
             Initialize the loader with the int ID constant, null bundle, and this
             activity for LoaderCallbacks since it implements LoaderCallbacks
              */
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            // Hide the progress bar as GONE so it doesn't take up any layout space
            mProgressBar.setVisibility(View.GONE);
            // Set the empty view on the GridView
            mGridView.setEmptyView(mEmptyStateTextView);
            // Display "No Internet Connection" as empty view text
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        // Create a new Loader for the request URL
        return new MovieLoader(this, POPULAR_MOVIES_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // If there is no list of movies, then do nothing
        if (movies == null) {
            return;
        }

        // Hide the progress bar as GONE so it doesn't take up any layout space
        mProgressBar.setVisibility(View.GONE);
        // Clear the adapter of previous movie data
        mAdapter.clear();
        // Add the list of Movie objects to the MovieAdapter
        mAdapter.addAll(movies);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Clear the adapter of movie data
        mAdapter.clear();
    }
}
