package com.apps.washington.moviemammal.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.apps.washington.moviemammal.constructor.Movie;
import com.apps.washington.moviemammal.utils.QueryUtils;

import java.util.List;

/**
 * Created by Brent on 3/18/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    /**
     * The request URL String
     */
    private String mUrl;

    /**
     * Constructs a new {@link MovieLoader}
     *
     * @param context The current context
     * @param url The request URL
     */
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Run on the background thread
     */
    @Override
    public List<Movie> loadInBackground() {
        // If the URL is null, then return nothing
        if (mUrl == null) {
            return null;
        }

        // Proceed to fetch and return the list of movie data
        List<Movie> movies = QueryUtils.fetchMovieData(mUrl);
        return movies;
    }
}
