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
        TextView title = (TextView) rowView.findViewById(R.id.track_row_title);
        TextView sub1 = (TextView) rowView.findViewById(R.id.track_row_sub1);
        TextView sub2 = (TextView) rowView.findViewById(R.id.track_row_sub2);

        T item = getItem(position);

        if (item instanceof Track) {
            title.setText(((Track) item).name);
            if (((Track) item).artists != null) sub1.setText(((Track) item).artists.get(0).name);
            if (((Track) item).album != null) sub2.setText(((Track) item).album.name);
        } else if (item instanceof SavedTrack) {
            title.setText(((SavedTrack) item).track.name);
            if (((SavedTrack) item).track.artists != null && ((SavedTrack) item).track.artists.size() > 0)
            sub1.setText(((SavedTrack) item).track.artists.get(0).name);
            if (((SavedTrack) item).track.album != null) sub2.setText(((SavedTrack) item).track.album.name);
        } else if (item instanceof Artist) {
            title.setText(((Artist) item).name);
        } else if (item instanceof Album) {
            title.setText(((Album) item).name);
            if (((Album) item).artists != null && ((Album) item).artists.size() > 0)
                 sub1.setText(((Album) item).artists.get(0).name);
            sub2.setText(((Album) item).release_date);
        } else if (item instanceof PlaylistSimple) {
            title.setText(((PlaylistSimple) item).name);
            sub1.setText(((PlaylistSimple) item).owner.display_name);
        } else if (item instanceof TrackSimple) {
            title.setText(((TrackSimple) item).name);
            if (((TrackSimple) item).artists != null && ((TrackSimple) item).artists.size() > 0)
             sub1.setText(((TrackSimple) item).artists.get(0).name);
        } else if (item instanceof String) {
            title.setText((String) item);
        }

        return rowView;
    }

}