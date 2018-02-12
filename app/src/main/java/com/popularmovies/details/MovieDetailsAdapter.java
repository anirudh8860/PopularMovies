package com.popularmovies.details;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by anirudhsohil on 10/02/18.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.MovieDetailsViewHolder> {
    private Context context;
    private String[] movieDetails, reviewVal, trailerVal;
    private LayoutInflater inflater;

    public MovieDetailsAdapter(Context context, String[] movieDetails, String[] reviewVal, String[] trailerVal){
        this.context = context;
        this.movieDetails = movieDetails;
        this.reviewVal = reviewVal;
        this.trailerVal = trailerVal;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MovieDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_details_view, parent, false);
        return new MovieDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDetailsViewHolder holder, int position) {
        Picasso.with(context)
                .load(movieDetails[3])
                .placeholder(R.drawable.pacman)
                .into(holder.moviePoster);

        holder.title.setText(movieDetails[2]);
        holder.plot.setText(movieDetails[4]);
        holder.rating.setText(movieDetails[5]+"/10");
        holder.releaseDate.setText(movieDetails[6]);

        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewVal);
        RecyclerView.LayoutManager reviewlayout = new LinearLayoutManager(context);
        reviewlayout.setAutoMeasureEnabled(true);
        holder.reviewList.setLayoutManager(reviewlayout);
        holder.reviewList.setAdapter(reviewAdapter);

        TrailerAdapter trailerAdapter = new TrailerAdapter(context, trailerVal);
        RecyclerView.LayoutManager trailerlayout = new LinearLayoutManager(context);
        reviewlayout.setAutoMeasureEnabled(true);
        holder.trailerList.setLayoutManager(trailerlayout);
        holder.trailerList.setAdapter(trailerAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MovieDetailsViewHolder extends RecyclerView.ViewHolder{
        final ImageView moviePoster;
        final TextView title, rating, releaseDate, plot;
        final RecyclerView reviewList, trailerList;

        public MovieDetailsViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            rating = (TextView) itemView.findViewById(R.id.rating);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            plot = (TextView) itemView.findViewById(R.id.plot);
            trailerList = (RecyclerView) itemView.findViewById(R.id.trailer_list);
            reviewList = (RecyclerView) itemView.findViewById(R.id.review_list);
        }
    }
}
