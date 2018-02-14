package com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popularmovies.fav.Favorites;

import java.net.URL;

public class Home extends AppCompatActivity{

    private final String POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";
    private final String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private final String CURR_URL_KEY = "curr_url";
    private final String CURR_MOVIE_DATA_KEY = "movie_data";
    private final String CURR_OP_DATA_KEY = "data_key";

    private String[] outputArray, moviesData;
    private ProgressBar loadingIndicator;
    URL popularMovieUrl, topRatedMovieUrl;
    TextView notConnectedText;
    RecyclerView recyclerView;
    MoviesDataAdapter moviesDataAdapter;
    private String RECYCLE_VIEW_POSITION = "curr_pos", CURR_URL_VAL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        notConnectedText = (TextView) findViewById(R.id.notConnectedText);

        popularMovieUrl = MovieNetworkUtils.buildUrl(POPULAR_URL);
        topRatedMovieUrl = MovieNetworkUtils.buildUrl(TOP_RATED_URL);

        if (CURR_URL_VAL.equals(""))
            CURR_URL_VAL = POPULAR_URL;

        if (savedInstanceState == null)
            showMovies(CURR_URL_VAL);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Parcelable currState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLE_VIEW_POSITION, currState);
        outState.putString(CURR_URL_KEY, CURR_URL_VAL);
        outState.putStringArray(CURR_MOVIE_DATA_KEY, moviesData);
        outState.putStringArray(CURR_OP_DATA_KEY, outputArray);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLE_VIEW_POSITION);
            CURR_URL_VAL = savedInstanceState.getString(CURR_URL_KEY);
            moviesData = savedInstanceState.getStringArray(CURR_MOVIE_DATA_KEY);
            outputArray = savedInstanceState.getStringArray(CURR_OP_DATA_KEY);

            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            moviesDataAdapter = new MoviesDataAdapter(getApplicationContext(), moviesData, outputArray);
            recyclerView.setAdapter(moviesDataAdapter);
        }
    }

    public void showMovies(String moviesUrl){
        if (isOnline()) {
            notConnectedText.setVisibility(View.GONE);
            URL url = MovieNetworkUtils.buildUrl(moviesUrl);
            new FetchMovieClass().execute(url);
        }
        else {
            notConnectedText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular){
            CURR_URL_VAL = POPULAR_URL;
            showMovies(POPULAR_URL);
            return true;
        }
        if (id == R.id.top_rated){
            CURR_URL_VAL = TOP_RATED_URL;
            showMovies(TOP_RATED_URL);
            return true;
        }
        if (id == R.id.showFavFromHome){
            Intent favIntent = new Intent(this, Favorites.class);
            startActivity(favIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class FetchMovieClass extends AsyncTask<URL, Void, String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL movieUrl = urls[0];

            try {
                String movieJson = MovieNetworkUtils.getResponseFromHttpUrl(movieUrl);
                String[] movieJsonArray = MovieJsonUtils.getMovieDetailsFromJson(getApplicationContext(), movieJson);
                return movieJsonArray;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieJsonArray) {
            super.onPostExecute(movieJsonArray);
            recyclerView.setVisibility(View.VISIBLE);
            moviesData = new String[movieJsonArray.length];
            outputArray = new String[movieJsonArray.length];
            outputArray = movieJsonArray;

//                for (int i = 0; i < movieJsonArray.length; i++)
//                    Log.d("Array", movieJsonArray[i]);

            for (int i = 0; i < movieJsonArray.length; i++) {
                String[] reqData = movieJsonArray[i].split("=");
//                    for (int p = 0; p < reqData.length; p++)
//                        Log.d("URL", p+":"+reqData[p]);
//                    Log.d("URL", reqData[1]);
                moviesData[i] = reqData[3];
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
//              Log.d("URL"+i, moviesData[i]);

            moviesDataAdapter = new MoviesDataAdapter(getApplicationContext(), moviesData, outputArray);
            recyclerView.setAdapter(moviesDataAdapter);
        }
    }
}
