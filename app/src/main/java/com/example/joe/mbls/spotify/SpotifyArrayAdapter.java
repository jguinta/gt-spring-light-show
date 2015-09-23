package com.example.joe.mbls.spotify;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by JOE on 9/23/2015.
 */
public class SpotifyArrayAdapter<T> extends ArrayAdapter<T> {

    public SpotifyArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SpotifyArrayAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.track_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.rowTextView);

        // Change icon based on name
        T track = getItem(position);

        if (track instanceof Track) {
            textView.setText(((Track) track).name);
        } else if (track instanceof SavedTrack) {
            textView.setText(((SavedTrack) track).track.name);
        }

        return rowView;
    }

}