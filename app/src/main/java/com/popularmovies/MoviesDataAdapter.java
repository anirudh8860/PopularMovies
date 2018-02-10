package com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by anirudhsohil on 06/01/18.
 */

public class MoviesDataAdapter extends RecyclerView.Adapter<MoviesDataAdapter.DataViewHolder> {

    private Context context;
    private String[] urls, opData;
    private LayoutInflater inflater;

    public class DataViewHolder extends RecyclerView.ViewHolder{
        final ImageView moviePosterView;

        public DataViewHolder(View itemView) {
            super(itemView);
            moviePosterView = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }

    public MoviesDataAdapter(Context context, String[] urls, String[] opData){
        this.context = context;
        this.urls = urls;
        this.opData = opData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movies_poster_view, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        Picasso.with(context)
                .load(urls[position])
                .placeholder(R.drawable.pacman)
                .fit()
                .into(holder.moviePosterView);
        holder.moviePosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (urls != null)
            return urls.length;
        else return 0;
    }

    private void openDetailsActivity(int position){
        Intent movieDetailsIntent = new Intent(context, MovieDetails.class);
        Log.d("Data passed", opData[position]);
        movieDetailsIntent.putExtra("movie_data", opData[position]);
        context.startActivity(movieDetailsIntent);
    }
}
