package com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class MovieContentProvider extends ContentProvider {

    private MovieDbHelper movieDbHelper;
    private static final int MOVIE = 542;
    private static final int MOVIE_WITH_ID = 543;

    private static UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher op = new UriMatcher(UriMatcher.NO_MATCH);
        op.addURI(MovieContract.AUTHORITY, MovieContract.taskPath, MOVIE);
        op.addURI(MovieContract.AUTHORITY, MovieContract.taskPath+"/#", MOVIE_WITH_ID);
        return op;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor retCursor;
        switch (match) {
            // Query for the tasks directory
            case MOVIE:
                retCursor =  database.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Log.d("Match", String.valueOf(match));

        Uri outputUri;
        switch (match){
            case MOVIE:
                long id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id >= 0)
                    outputUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new android.database.SQLException("Failed to insert row" + uri);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("Outputuri", String.valueOf(outputUri));
        return outputUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Log.d("Match", String.valueOf(match));

        int totalDeletions;
        switch (match){
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                totalDeletions = database.delete(MovieContract.MovieEntry.TABLE_NAME,
                        "_id=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented " + uri);
        }

        if (totalDeletions != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return totalDeletions;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
