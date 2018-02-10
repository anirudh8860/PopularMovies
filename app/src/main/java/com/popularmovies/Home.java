package com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popularmovies.fav.Favorites;

import java.net.URL;

public class Home extends AppCompatActivity {

    private final String POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";
    private final String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";

    private String[] outputArray, moviesData;
    GridView gridView;
    private ProgressBar loadingIndicator;
    URL popularMovieUrl, topRatedMovieUrl;
    TextView notConnectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridView = (GridView) findViewById(R.id.movies_grid_view);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        notConnectedText = (TextView) findViewById(R.id.notConnectedText);

        popularMovieUrl = MovieNetworkUtils.buildUrl(POPULAR_URL);
        topRatedMovieUrl = MovieNetworkUtils.buildUrl(TOP_RATED_URL);

        showPopularMovies();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieDetailsIntent = new Intent(Home.this, MovieDetails.class);
                movieDetailsIntent.putExtra("movie_data", outputArray[position]);
                startActivity(movieDetailsIntent);
            }
        });
    }

    public void showPopularMovies(){
        if (isOnline()) {
            notConnectedText.setVisibility(View.GONE);
            new FetchMovieClass().execute(popularMovieUrl);
        }
        else {
            notConnectedText.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }

    public void showTopRatedMovies(){
        if (isOnline()) {
            notConnectedText.setVisibility(View.GONE);
            new FetchMovieClass().execute(topRatedMovieUrl);
        }
        else {
            notConnectedText.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
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
            showPopularMovies();
            return true;
        }
        if (id == R.id.top_rated){
            showTopRatedMovies();
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
            gridView.setVisibility(View.VISIBLE);
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
            for (int i = 0; i < moviesData.length; i++)
                Log.d("URL"+i, moviesData[i]);

            gridView.setAdapter(new MoviesDataAdapter(Home.this, moviesData));
        }
    }
}
