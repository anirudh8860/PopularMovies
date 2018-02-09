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

    private static final String RESULTS = "results";

    private static final String ID = "id";
    private static final String TITLE = "original_title";
    private static final String PLOT = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String STATUS_CODE = "status_code";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private static final String KEY = "key";

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
            String id = "", posterPath = "", title = "", plot = "", rating = "", releaseDate = "";
            Log.d("MovieArr", String.valueOf(movieJsonArray));
            for (int i = 0; i < movieJsonArray.length(); i++){
                JSONObject movieObject = movieJsonArray.getJSONObject(i);
                //Log.d("Object", String.valueOf(movieObject));
                id = movieObject.getString(ID);
                posterPath = movieObject.getString(POSTER_PATH);
                title = movieObject.getString(TITLE);
                plot = movieObject.getString(PLOT);
                rating = movieObject.getString(RATING);
                releaseDate = movieObject.getString(RELEASE_DATE);
                parsedMovieData[i] =i
                        +"="+id
                        +"="+title
                        +"="+IMAGE_URL+posterPath
                        +"="+plot
                        +"="+rating
                        +"="+releaseDate;
                Log.d("DATA", parsedMovieData[i]);
            }
        }
        return parsedMovieData;
    }

    public static String[] getReviewDetailsFromJson(Context context, String reviewJsonStr) throws JSONException{
        String[] parsedReviewData = null;

        JSONObject reviewJsonObject = new JSONObject(reviewJsonStr);
        //Log.d("Object", String.valueOf(movieJsonObject));
        if (reviewJsonObject.has(STATUS_CODE)){
            return null;
        }
        else{
            JSONArray reviewJsonArray = reviewJsonObject.getJSONArray(RESULTS);
            parsedReviewData = new String[reviewJsonArray.length()];
            String author = "", content = "";
            Log.d("ReviewArr", String.valueOf(reviewJsonArray));
            for (int i = 0; i < reviewJsonArray.length(); i++){
                JSONObject reviewObject = reviewJsonArray.getJSONObject(i);
                author = reviewObject.getString(AUTHOR);
                content = reviewObject.getString(CONTENT);
                parsedReviewData[i] = author + "=" + content;
                Log.d("DATA", parsedReviewData[i]);
            }
        }

        return parsedReviewData;
    }

    public static String[] getTrailerFromJson(Context context, String trailerJsonStr) throws JSONException{
        String[] parsedTrailerData = null;

        JSONObject trailerJsonObject = new JSONObject(trailerJsonStr);
        //Log.d("Object", String.valueOf(movieJsonObject));
        if (trailerJsonObject.has(STATUS_CODE)){
            return null;
        }
        else{
            JSONArray trailerJsonArray = trailerJsonObject.getJSONArray(RESULTS);
            parsedTrailerData = new String[trailerJsonArray.length()];
            String key = "";
            Log.d("ReviewArr", String.valueOf(trailerJsonArray));
            for (int i = 0; i < trailerJsonArray.length(); i++){
                JSONObject trailerObject = trailerJsonArray.getJSONObject(i);
                key = trailerObject.getString(KEY);
                parsedTrailerData[i] = key;
                Log.d("VIDEO", parsedTrailerData[i]);
            }
        }

        return parsedTrailerData;
    }
}
