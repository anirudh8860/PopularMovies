package com.popularmovies.details;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.MovieJsonUtils;
import com.popularmovies.MovieNetworkUtils;
import com.popularmovies.R;
import com.popularmovies.fav.Favorites;
import com.popularmovies.data.MovieContract;

import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    String movieDetails;
    private final String BASE_URL = "http://api.themoviedb.org/3/movie/",
            APPEND_REVIEWS = "/reviews",
            APPEND_TRAILERS = "/videos";
    String[] splitMovieDetails,
            reviewVal = new String[0],
            trailerVal = new String[0];
    RecyclerView recyclerView;
    MovieDetailsAdapter movieDetailsAdapter;
    String RECYCLE_VIEW_POSITION = "curr_pos";
    TextView notConnectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        recyclerView = (RecyclerView) findViewById(R.id.movie_details_recycler_view);
        notConnectedText = (TextView) findViewById(R.id.notConnectedTextDetails);

        Intent movieDetailsIntent = getIntent();
        movieDetails = movieDetailsIntent.getStringExtra("movie_data");

        splitMovieDetails = movieDetails.split("=");
//        for (int i = 0; i < splitMovieDetails.length; i++)
//            Log.d("Movie Details", splitMovieDetails[i]);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        manager.setAutoMeasureEnabled(true);
        showMovieDetails();
        recyclerView.setLayoutManager(manager);
    }

    private void showMovieDetails() {
        if (isOnline()){
            notConnectedText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            new GetReviews().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[1] + APPEND_REVIEWS));
            new GetTrailers().execute(MovieNetworkUtils.buildUrl(BASE_URL + splitMovieDetails[1] + APPEND_TRAILERS));
        }
        else {
            notConnectedText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable currState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLE_VIEW_POSITION, currState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLE_VIEW_POSITION);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
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
        contentValues.put(MovieContract.MovieEntry.MOVIE_YEAR, splitMovieDetails[6]);
        contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE, splitMovieDetails[3]);
        contentValues.put(MovieContract.MovieEntry.MOVIE_DATA, movieDetails);

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
            for (int i = 0; i < strings.length; i++) {
                Log.d("Strings", strings[i]);
            }
            reviewVal = strings;

//            movieDetailsAdapter = new MovieDetailsAdapter(
//                    getApplicationContext(),
//                    splitMovieDetails,
//                    reviewVal,
//                    trailerVal);
//            recyclerView.setAdapter(movieDetailsAdapter);
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
            for (int i = 0; i < strings.length; i++) {
                Log.d("Strings", strings[i]);
            }
            trailerVal = strings;
            movieDetailsAdapter = new MovieDetailsAdapter(
                    getApplicationContext(),
                    splitMovieDetails,
                    reviewVal,
                    trailerVal);
            recyclerView.setAdapter(movieDetailsAdapter);
            //ShowFullList.setListViewHeightBasedOnItems(trailerList);
        }
    }

}

