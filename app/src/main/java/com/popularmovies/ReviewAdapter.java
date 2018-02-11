package com.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    String[] review;
    Context context;
    LayoutInflater inflater;

    public ReviewAdapter(Context context, String[] review){
        this.context = context;
        this.review = review;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String[] op = review[position].split("=");
        holder.userName.setText(op[0]);
        holder.userReview.setText(op[1]);
    }

    @Override
    public int getItemCount() {
        if (review != null)
            return review.length;
        else
            return 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        final TextView userName, userReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userReview = (TextView) itemView.findViewById(R.id.user_review);
        }
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        TextView userName, userReview;
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.user_review_layout, parent, false);
//
//        String[] reviewSplit = review[position].split("=");
//        userName = convertView.findViewById(R.id.user_name);
//        userReview = convertView.findViewById(R.id.user_review);
//
//        userName.setText(reviewSplit[0]);
//        userReview.setText(reviewSplit[1]);
//
//        return convertView;
//    }
}
