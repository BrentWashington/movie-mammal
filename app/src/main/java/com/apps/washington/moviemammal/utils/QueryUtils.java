package com.apps.washington.moviemammal.utils;

/**
 * Created by Brent on 3/5/2018.
 */

import android.net.Uri;
import android.util.Log;

import com.apps.washington.moviemammal.constructor.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing helper methods related to requesting and receiving
 * movie data from The Movie Database
 */
public final class QueryUtils {

    /**
     * Log tag
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Private constructor that stops the instantiation of {@link QueryUtils}.
     *
     * This class does not need to be instantiated because it is only needed to
     * hold static variables and methods
     */
    private QueryUtils() {
    }

    /**
     * The base URL for the movie poster
     *
     * The size is w185
     *
     * The value extracted from the key "poster_path" will be appended to this URL for
     * the movie poster
     */
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    /**
     * Query The Movie Database and return a list of {@link Movie} objects
     */
    public static List<Movie> fetchMovieData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create a list of movies
        List<Movie> movies = extractResultsFromJson(jsonResponse);

        // Return the list of movies
        return movies;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Parse the popular movies JSON response and return an {@link Movie} object
     * from the input movieJSON String
     */
    public static ArrayList<Movie> extractResultsFromJson(String movieJSON) {

        // Create a new ArrayList to add movies to
        ArrayList<Movie> popularMoviesList = new ArrayList<>();

        try {
            // Create a new JSONObject with the JSON request URL
            JSONObject jsonResponse = new JSONObject(movieJSON);
            // Get the JSONArray "results"
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            /*
             Starting at position 0, increment through each result until the end
             of the JSONArray is reached
              */
            for (int i = 0; i < resultsArray.length(); i++) {
                /*
                 Convert the JSONArray into a JSONObject that starts at the beginning of
                 the JSONArray
                  */
                JSONObject firstResult = resultsArray.getJSONObject(i);

                // Extract the value at the key called "title"
                String title = firstResult.getString("title");
                // Extract the value at the key called "vote_average"
                String voteAverage = firstResult.getString("vote_average");
                // Extract the value at the key called "poster_path"
                String posterPath = firstResult.getString("poster_path");
                /*
                 Create a String combining the base URL and extracted poster path, which
                 creates the URL for each movie poster
                  */
                String moviePoster = POSTER_BASE_URL + posterPath;
                // Convert the movie poster String into a Uri object
                Uri posterUri = Uri.parse(moviePoster);

                /*
                 Create a new Movie object with the title, vote average, and poster from
                 the JSON response
                  */
                Movie movie = new Movie(title, voteAverage, posterUri);

                // Add the Movie object to the popular movies ArrayList
                popularMoviesList.add(movie);
            }
            // Catch a JSONException and print to logs if there was a parsing error
            // Stops the app from crashing
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movie data", e);
        }

        // Return the list of popular movies
        return popularMoviesList;
    }
}
