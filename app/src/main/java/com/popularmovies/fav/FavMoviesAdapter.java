package com.popularmovies.fav;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.details.MovieDetails;
import com.popularmovies.R;
import com.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by anirudhsohil on 09/02/18.
 */

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.FavViewHolder>{

    private static final String TAG = FavMoviesAdapter.class.getSimpleName();
    private Context context;
    private Cursor cursor;
    private final LayoutInflater inflater;

    class FavViewHolder extends RecyclerView.ViewHolder{
        final TextView favMovieName;
        //, favMovieRelYear;
        final ImageView favPoster;

        public FavViewHolder(View itemView) {
            super(itemView);
            favMovieName = (TextView) itemView.findViewById(R.id.fav_movie_name);
            //favMovieRelYear = (TextView) itemView.findViewById(R.id.fav_movie_year);
            favPoster = (ImageView) itemView.findViewById(R.id.fav_poster);
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

//                int movieYearIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_YEAR);
//                String movieYear = cursor.getString(movieYearIndex);

                int favPosterIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_IMAGE);
                String favMovieImageUrl = cursor.getString(favPosterIndex);

                holder.itemView.setTag(id);
                holder.favMovieName.setText(movieName);
                //holder.favMovieRelYear.setText(movieYear.substring(0,4));

                Picasso.with(context)
                        .load(favMovieImageUrl)
                        .placeholder(R.drawable.pacman)
                        .into(holder.favPoster);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int movieInfoIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_DATA);
                        openMovieDetails(cursor.getString(movieInfoIndex));
                    }
                });
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

    public void openMovieDetails(String info){
        Intent movieDetailsIntent = new Intent(context, MovieDetails.class);
        Log.d("Data passed", info);
        movieDetailsIntent.putExtra("movie_data", info);
        context.startActivity(movieDetailsIntent);
    }
}
