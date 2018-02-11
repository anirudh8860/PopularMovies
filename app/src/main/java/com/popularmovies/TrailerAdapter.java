package com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    Context context;
    String[] videoIds;
    LayoutInflater inflater;

    public TrailerAdapter(Context context, String[] videoIds){
        this.context = context;
        this.videoIds = videoIds;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trailer_layout, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        holder.videoText.setText("Trailer "+ (position+1));
        holder.videoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrailer(videoIds[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videoIds != null)
            return videoIds.length;
        else
            return 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{

        final TextView videoText;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            videoText = (TextView) itemView.findViewById(R.id.trailer_text);
        }
    }

    private void openTrailer(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + id));
            context.startActivity(i);
        }
    }
}
