package com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.popularmovies";
    public static final Uri baseUri = Uri.parse("content://" + AUTHORITY);
    public static final String taskPath = "tasks";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = baseUri.buildUpon().appendPath(taskPath).build();
        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_NAME = "title";
        public static final String MOVIE_YEAR = "movie_year";
        //public static final String MOVIE_IMAGE = "image_url";
    }
}
