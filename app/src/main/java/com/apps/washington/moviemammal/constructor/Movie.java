package com.apps.washington.moviemammal.constructor;

/**
 * Created by Brent on 3/5/2018.
 */

import android.net.Uri;

/**
 * Constructor class for the Movie object
 */
public class Movie {

    /**
     * The title of the movie
     */
    private String mMovieTitle;

    /**
     * The vote average for the movie
     */
    private String mMovieVoteAverage;

    /**
     * The poster for the movie
     */
    private Uri mPoster;

    /**
     * The constructor class for the {@link Movie} object
     *
     * @param movieTitle The title of the movie
     * @param movieVoteAverage The vote average of the movie
     * @param poster The poster for the movie
     */
    public Movie(String movieTitle, String movieVoteAverage, Uri poster) {
        mMovieTitle = movieTitle;
        mMovieVoteAverage = movieVoteAverage;
        mPoster = poster;
    }

    // Get the movie title
    public String getMovieTitle() {
        return mMovieTitle;
    }

    // Get the movie's vote average
    public String getMovieVoteAverage() {
        return mMovieVoteAverage;
    }

    // Get the movie's poster
    public Uri getPoster() {
        return mPoster;
    }
}
