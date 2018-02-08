package com.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class ReviewAdapter extends ArrayAdapter{
    String[] review;
    Context context;
    LayoutInflater inflater;

    public ReviewAdapter(Context context, String[] review){
        super(context, R.layout.user_review_layout, review);
        this.context = context;
        this.review = review;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView userName, userReview;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.user_review_layout, parent, false);

        String[] reviewSplit = review[position].split("=");
        userName = convertView.findViewById(R.id.user_name);
        userReview = convertView.findViewById(R.id.user_review);

        userName.setText(reviewSplit[0]);
        userReview.setText(reviewSplit[1]);

        return convertView;
    }
}
