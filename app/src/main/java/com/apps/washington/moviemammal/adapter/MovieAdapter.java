package com.apps.washington.moviemammal.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.washington.moviemammal.R;
import com.apps.washington.moviemammal.constructor.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Brent on 3/5/2018.
 */

/**
 * ArrayAdapter class that populates the list with Movie objects
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * Custom Constructor for the adapter
     *
     * @param context The current context. Used to inflate the layout file
     * @param movies  A list of Movie objects to display in the list
     */
    public MovieAdapter(Activity context, ArrayList<Movie> movies) {
        // Initialize the ArrayAdapter storage for the context and the list
        super(context, 0, movies);
    }

    /**
     * Provides a view for an AdapterView (e.g. ListView)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view
     * @param convertView The recycled view to populate
     * @param parent      The parent ViewGroup that is used for inflation
     * @return The View for the position in the AdapterView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the Movie object located at this position in the list
        Movie currentMovie = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID "movieTitle"
        TextView titleTextView = listItemView.findViewById(R.id.movieTitle);
        /*
         Get the movie title from the current Movie object and set it on the
         title TextView
          */
        titleTextView.setText(currentMovie.getMovieTitle());

        TextView voteAverageTextView = listItemView.findViewById(R.id.movieVoteAverage);
        voteAverageTextView.setText(currentMovie.getMovieVoteAverage());

        // Find the ImageView in the list_item.xml layout with the ID "moviePoster"
        ImageView poster = listItemView.findViewById(R.id.moviePoster);
        // Create a URI object with the poster URI for the current movie
        Uri posterUri = currentMovie.getPoster();
        /*
         With Picasso, get the context, load the posterUri, and convert it into
         an ImageView
          */
        Picasso.with(getContext()).load(posterUri).into(poster);

        // Return the list item view
        return listItemView;
    }
}
