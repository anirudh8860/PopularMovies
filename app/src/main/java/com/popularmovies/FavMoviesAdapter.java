package com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularmovies.data.MovieContract;

/**
 * Created by anirudhsohil on 09/02/18.
 */

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.FavViewHolder> {

    private static final String TAG = FavMoviesAdapter.class.getSimpleName();
    private Context context;
    private Cursor cursor;
    private final LayoutInflater inflater;

    class FavViewHolder extends RecyclerView.ViewHolder{
        public final TextView favMovieName;

        public FavViewHolder(View itemView) {
            super(itemView);
            favMovieName = (TextView) itemView.findViewById(R.id.fav_movie_name);
        }
    }

    public FavMoviesAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.fav_movie_details, parent, false);
        return new FavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
                int id = cursor.getInt(idIndex);

                int movieIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_NAME);
                String movieName = cursor.getString(movieIndex);

                holder.itemView.setTag(id);
                holder.favMovieName.setText(movieName);
            }
            else {
                holder.favMovieName.setText("Movie name cannot be displayed ");
            }
        }
        else {
            Log.e (TAG, "onBindViewHolder: Cursor is null.");
        }
    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        else return 0;
    }

    public void setData(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
