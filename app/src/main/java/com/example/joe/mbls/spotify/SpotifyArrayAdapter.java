package com.example.joe.mbls.spotify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

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
        T item = getItem(position);

        if (item instanceof Track) {
            textView.setText(((Track) item).name);
        } else if (item instanceof SavedTrack) {
            textView.setText(((SavedTrack) item).track.name);
        } else if (item instanceof Artist) {
            textView.setText(((Artist) item).name);
        } else if (item instanceof Album) {
            textView.setText(((Album) item).name);
        } else if (item instanceof PlaylistSimple) {
            textView.setText(((PlaylistSimple) item).name);
        } else if (item instanceof TrackSimple) {
            textView.setText(((TrackSimple) item).name);
        }

        return rowView;
    }

}