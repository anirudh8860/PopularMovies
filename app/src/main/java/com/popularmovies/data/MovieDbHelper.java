package com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movieDb.db";
    private static final int VERSION = 1;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME,null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.MOVIE_ID + " REAL NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_NAME + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_YEAR + " TEXT NOT NULL);";
                //MovieContract.MovieEntry.MOVIE_IMAGE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
