package com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    ImageView posterView;
    TextView title, rating, releaseDate, plot;
    String movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        posterView = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        rating = (TextView) findViewById(R.id.rating);
        releaseDate = (TextView) findViewById(R.id.release_date);
        plot = (TextView) findViewById(R.id.plot);

        Intent movieDetailsIntent = getIntent();
        movieDetails = movieDetailsIntent.getStringExtra("movie_data");

        String[] splitMovieDetails = movieDetails.split("=");
//        for (int i = 0; i < splitMovieDetails.length; i++)
//            Log.d("Movie Details", splitMovieDetails[i]);

        Picasso.with(getApplicationContext())
                .load(splitMovieDetails[1])
                .placeholder(R.drawable.pacman)
                .into(posterView);

        title.setText(splitMovieDetails[0]);
        plot.setText(splitMovieDetails[2]);
        rating.setText(splitMovieDetails[3]);
        releaseDate.setText(splitMovieDetails[4]);
    }
}
