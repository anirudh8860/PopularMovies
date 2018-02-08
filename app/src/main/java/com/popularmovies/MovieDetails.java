package com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    ImageView posterView;
    TextView title, rating, releaseDate, plot;
    ListView trailerList, reviewList;
    String movieDetails;
    private final String BASE_URL = "http://api.themoviedb.org/3/movie/"
            , APPEND_REVIEWS = "/reviews"
            , APPEND_TRAILERS = "/videos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        posterView = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        rating = (TextView) findViewById(R.id.rating);
        releaseDate = (TextView) findViewById(R.id.release_date);
        plot = (TextView) findViewById(R.id.plot);
        trailerList = (ListView) findViewById(R.id.trailer_list);
        reviewList = (ListView) findViewById(R.id.review_list);

        Intent movieDetailsIntent = getIntent();
        movieDetails = movieDetailsIntent.getStringExtra("movie_data");

        String[] splitMovieDetails = movieDetails.split("=");
//        for (int i = 0; i < splitMovieDetails.length; i++)
//            Log.d("Movie Details", splitMovieDetails[i]);

        Picasso.with(getApplicationContext())
                .load(splitMovieDetails[2])
                .placeholder(R.drawable.pacman)
                .into(posterView);

        title.setText(splitMovieDetails[1]);
        plot.setText(splitMovieDetails[3]);
        rating.setText(splitMovieDetails[4]);
        releaseDate.setText(splitMovieDetails[5]);
        new GetReviews().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[0] + APPEND_REVIEWS));
        new GetTrailers().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[0] + APPEND_TRAILERS));
    }

    class GetReviews extends AsyncTask<URL, Void, String[]>{

        @Override
        protected String[] doInBackground(URL... urls) {
            URL reviewUrl = urls[0];
            try{
                String reviewJson = MovieNetworkUtils.getResponseFromHttpUrl(reviewUrl);
                String[] reviewJsonArray = MovieJsonUtils.getReviewDetailsFromJson(getApplicationContext(), reviewJson);
                return reviewJsonArray;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            for (int i = 0; i < strings.length; i++) {
                Log.d("Strings", strings[i]);
            }

            reviewList.setAdapter(new ReviewAdapter(getApplicationContext(), strings));
        }
    }

    class GetTrailers extends AsyncTask<URL, Void, String[]>{

        @Override
        protected String[] doInBackground(URL... urls) {
            URL trailerUrl = urls[0];
            try{
                String trailerJson = MovieNetworkUtils.getResponseFromHttpUrl(trailerUrl);
                String[] trailerJsonArray = MovieJsonUtils.getTrailerFromJson(getApplicationContext(), trailerJson);
                return trailerJsonArray;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            trailerList.setAdapter(new TrailerAdapter(getApplicationContext(), strings));
        }
    }
}
