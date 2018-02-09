package com.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    ImageView posterView;
    TextView title, rating, releaseDate, plot;
    ListView trailerList, reviewList;
    LinearLayout trailerListToLinear, reviewListToLinear;
    String movieDetails;
    private final String BASE_URL = "http://api.themoviedb.org/3/movie/"
            , APPEND_REVIEWS = "/reviews"
            , APPEND_TRAILERS = "/videos";
    String[] splitMovieDetails;
    ReviewAdapter reviewAdapter;

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

        splitMovieDetails = movieDetails.split("=");
//        for (int i = 0; i < splitMovieDetails.length; i++)
//            Log.d("Movie Details", splitMovieDetails[i]);

        Picasso.with(getApplicationContext())
                .load(splitMovieDetails[3])
                .placeholder(R.drawable.pacman)
                .into(posterView);

        title.setText(splitMovieDetails[2]);
        plot.setText(splitMovieDetails[4]);
        rating.setText(splitMovieDetails[5]);
        releaseDate.setText(splitMovieDetails[6]);
        new GetReviews().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[1] + APPEND_REVIEWS));
        new GetTrailers().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[1] + APPEND_TRAILERS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.addAsFav:
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.MOVIE_ID},
                        "movie_id=?",
                        new String[]{splitMovieDetails[1]},
                        null);
                if (cursor.getCount() != 0) {
                    Toast.makeText(this, "Already in Favories", Toast.LENGTH_LONG).show();
                }
                else onClickAddTask();
                cursor.close();
                break;

            case R.id.showFav:
                Intent favIntent = new Intent(this, Favorites.class);
                startActivity(favIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void onClickDelTask(String id) {
//        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(id).build();
//        Log.d("URI", uri.toString());
//        if (uri != null) {
//            Toast.makeText(this, "Deleted from Favourites", Toast.LENGTH_LONG).show();
//            getContentResolver().delete(uri, null, null);
//        }
//    }

    private void onClickAddTask(){
        ContentValues contentValues = new ContentValues();

        //contentValues.put(MovieContract.MovieEntry._ID, splitMovieDetails[0]);
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID, splitMovieDetails[1]);
        contentValues.put(MovieContract.MovieEntry.MOVIE_NAME, splitMovieDetails[2]);
        //contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE, splitMovieDetails[3]);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri != null)
            Toast.makeText(this, uri +" Added to Favourites", Toast.LENGTH_LONG).show();
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
            reviewAdapter = new ReviewAdapter(getApplicationContext(), strings);
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            for (int i = 0; i < strings.length; i++) {
                Log.d("Strings", strings[i]);
            }

            reviewList.setAdapter(new ReviewAdapter(getApplicationContext(), strings));
            //ShowFullList.setListViewHeightBasedOnItems(reviewList);
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
            ShowFullList.setListViewHeightBasedOnItems(trailerList);
        }
    }

}

