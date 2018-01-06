package com.popularmovies;

import android.content.Context;
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

public class MoviesDataAdapter extends ArrayAdapter {

    private Context context;
    private String[] urls;
    private LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;

    public MoviesDataAdapter(@NonNull Context context, @NonNull String[] urls) {
        super(context, R.layout.movies_poster_view, urls);
        this.context = context;
        this.urls = urls;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movies_poster_view, parent, false);
        }

        Log.d("Imageurl"+position, urls[position]);

        imageView = convertView.findViewById(R.id.movie_poster);
        Picasso.with(context)
                .load(urls[position])
                .placeholder(R.drawable.pacman)
                .fit()
                .into(imageView);

        return convertView;
    }
}
