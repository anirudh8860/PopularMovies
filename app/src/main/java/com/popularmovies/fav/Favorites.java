package com.popularmovies.fav;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.popularmovies.R;
import com.popularmovies.data.MovieContract;


public class Favorites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView favRecyclerView;
    private FavMoviesAdapter favMoviesAdapter;
    private static final String TAG = Favorites.class.getSimpleName();
    private static final int LOADER_ID = 544;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler_view);
        favMoviesAdapter = new FavMoviesAdapter(this);

        favRecyclerView.setAdapter(favMoviesAdapter);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                Log.d("ID", String.valueOf(id));

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);

                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(LOADER_ID, null, Favorites.this);
            }
        }).attachToRecyclerView(favRecyclerView);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor taskData = null;

            @Override
            protected void onStartLoading() {
                if (taskData != null)
                    deliverResult(taskData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {try {
                return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,null);

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }
            }

            @Override
            public void deliverResult(Cursor data) {
                taskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favMoviesAdapter.setData(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favMoviesAdapter.setData(null);
    }
}
