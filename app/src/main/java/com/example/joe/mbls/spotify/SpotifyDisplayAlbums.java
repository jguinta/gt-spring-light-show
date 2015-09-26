package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.spotify.sdk.android.player.Player;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class SpotifyDisplayAlbums extends Activity {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<Album> items = new ArrayList<Album>();

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items_list);

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();

        listView = (ListView) findViewById(R.id.responseView);


        adapter = new SpotifyArrayAdapter<Album>(this, R.layout.track_row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = (Album) adapter.getItem(position);
                Intent albumIntent = new Intent(getApplicationContext(), SpotifyDisplaySongs.class);

                albumIntent.putExtra("album", album.id);

                Log.d("Albums", "calling display songs with album");
                startActivity(albumIntent);

            }
        });

        new RetrieveAlbumsTask(getIntent().getStringExtra("artist")).execute();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class RetrieveAlbumsTask extends AsyncTask<Void, Void, String> {

        private String artist;

        public RetrieveAlbumsTask(String artist) {
            this.artist = artist;
        }

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {


            Pager<Album> albums = spotifyService.getArtistAlbums(artist);
            //tracks = new ArrayList<Track>();
            items.addAll(albums.items);
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }


}