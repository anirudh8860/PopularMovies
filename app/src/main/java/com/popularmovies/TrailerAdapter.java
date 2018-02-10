package com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anirudhsohil on 08/02/18.
 */

public class TrailerAdapter extends ArrayAdapter{

    Context context;
    String[] videoIds;
    LayoutInflater inflater;

    public TrailerAdapter(Context context, String[] videoIds){
        super(context, R.layout.trailer_layout, videoIds);
        this.context = context;
        this.videoIds = videoIds;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView trailerCountView;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.trailer_layout, parent, false);

        trailerCountView = convertView.findViewById(R.id.trailer_text);
        trailerCountView.setText("Trailer " + String.valueOf(position+1));
        Log.d("Trailer", String.valueOf(position+1));
        trailerCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrailer(videoIds[position]);
            }
        });

        return convertView;
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
