package com.popularmovies;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anirudhsohil on 05/01/18.
 */

public class MovieJsonUtils {

    static final String RESULTS = "results";
    static final String TITLE = "original_title";
    static final String PLOT = "overview";
    static final String RATING = "vote_average";
    static final String RELEASE_DATE = "release_date";
    static final String POSTER_PATH = "poster_path";
    static final String STATUS_CODE = "status_code";
    static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";


    public static String[] getMovieDetailsFromJson(Context context, String movieJsonStr) throws JSONException {
        String[] parsedMovieData = null;

        JSONObject movieJsonObject = new JSONObject(movieJsonStr);
        //Log.d("Object", String.valueOf(movieJsonObject));
        if (movieJsonObject.has(STATUS_CODE)){
            return null;
        }
        else{
            JSONArray movieJsonArray = movieJsonObject.getJSONArray(RESULTS);
            parsedMovieData = new String[movieJsonArray.length()];
            String posterPath = "", title = "", plot = "", rating = "", releaseDate = "";
            //Log.d("MovieArr", String.valueOf(movieJsonArray));
            for (int i = 0; i < movieJsonArray.length(); i++){
                JSONObject movieObject = movieJsonArray.getJSONObject(i);
                //Log.d("Object", String.valueOf(movieObject));
                posterPath = movieObject.getString(POSTER_PATH);
                title = movieObject.getString(TITLE);
                plot = movieObject.getString(PLOT);
                rating = movieObject.getString(RATING);
                releaseDate = movieObject.getString(RELEASE_DATE);
                parsedMovieData[i] = title
                        +"="+IMAGE_URL+posterPath
                        +"="+plot
                        +"="+rating
                        +"="+releaseDate;
                //Log.d("DATA", parsedMovieData[i]);
            }
        }
        return parsedMovieData;
    }
}
